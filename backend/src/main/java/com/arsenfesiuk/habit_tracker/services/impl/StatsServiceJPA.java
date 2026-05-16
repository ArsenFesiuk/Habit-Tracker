package com.arsenfesiuk.habit_tracker.services.impl;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.HabitEntry;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitBreakdownDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.StatsDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.WeeklyTrendPointDTO;
import com.arsenfesiuk.habit_tracker.repositories.HabitEntryRepository;
import com.arsenfesiuk.habit_tracker.repositories.HabitRepository;
import com.arsenfesiuk.habit_tracker.services.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceJPA implements StatsService {

    private final HabitRepository habitRepository;
    private final HabitEntryRepository habitEntryRepository;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @Override
    public StatsDTO getStats(LocalDate from, LocalDate to) {
        User user = getCurrentUser();
        List<Habit> habits = habitRepository.findAllByUserId(user.getId());

        if (habits.isEmpty()) {
            return new StatsDTO(0, 0, 0, 0, List.of(), List.of());
        }

        List<HabitEntry> entries = habitEntryRepository.findByHabitInAndDateBetween(habits, from, to);

        long daysInRange = ChronoUnit.DAYS.between(from, to) + 1;
        long totalSlots = (long) habits.size() * daysInRange;
        long doneCount = entries.stream().filter(e -> e.getCount() > 0).count();

        int completionRate = (int) Math.round(100.0 * doneCount / totalSlots);
        int totalCompletions = (int) doneCount;
        int activeHabits = habits.size();

        int bestCurrentStreak = habits.stream()
                .mapToInt(h -> computeCurrentStreak(h.getEntries()))
                .max()
                .orElse(0);

        List<WeeklyTrendPointDTO> weeklyTrend = computeWeeklyTrend(habits, entries, from, to);

        List<HabitBreakdownDTO> habitBreakdown = habits.stream()
                .map(h -> {
                    long habitDone = entries.stream()
                            .filter(e -> e.getHabit().getId().equals(h.getId()) && e.getCount() > 0)
                            .count();
                    int rate = (int) Math.round(100.0 * habitDone / daysInRange);
                    return new HabitBreakdownDTO(h.getName(), h.getColor(), rate);
                })
                .toList();

        return new StatsDTO(completionRate, totalCompletions, activeHabits,
                bestCurrentStreak, weeklyTrend, habitBreakdown);
    }

    private int computeCurrentStreak(List<HabitEntry> entries) {
        Set<LocalDate> doneDates = entries.stream()
                .filter(e -> e.getCount() > 0)
                .map(HabitEntry::getDate)
                .collect(Collectors.toSet());
        int streak = 0;
        LocalDate cursor = LocalDate.now().minusDays(1);
        while (doneDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private List<WeeklyTrendPointDTO> computeWeeklyTrend(
            List<Habit> habits, List<HabitEntry> allEntries, LocalDate from, LocalDate to) {
        // Monday on or before `from`
        int dow = from.getDayOfWeek().getValue(); // 1=Mon, 7=Sun
        LocalDate weekMonday = from.minusDays(dow - 1);

        List<WeeklyTrendPointDTO> points = new ArrayList<>();
        LocalDate cursor = weekMonday;
        while (!cursor.isAfter(to)) {
            LocalDate weekEnd = cursor.plusDays(6);
            LocalDate clampedStart = cursor.isBefore(from) ? from : cursor;
            LocalDate clampedEnd = weekEnd.isAfter(to) ? to : weekEnd;

            long daysInWeek = ChronoUnit.DAYS.between(clampedStart, clampedEnd) + 1;
            long slots = (long) habits.size() * daysInWeek;

            LocalDate fStart = clampedStart;
            LocalDate fEnd = clampedEnd;
            long done = allEntries.stream()
                    .filter(e -> e.getCount() > 0
                            && !e.getDate().isBefore(fStart)
                            && !e.getDate().isAfter(fEnd))
                    .count();

            int rate = slots == 0 ? 0 : (int) Math.round(100.0 * done / slots);
            points.add(new WeeklyTrendPointDTO(cursor, rate));
            cursor = cursor.plusWeeks(1);
        }
        return points;
    }
}
