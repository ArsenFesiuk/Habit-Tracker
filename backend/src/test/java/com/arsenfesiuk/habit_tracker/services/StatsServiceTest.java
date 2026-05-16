package com.arsenfesiuk.habit_tracker.services;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.HabitEntry;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.StatsDTO;
import com.arsenfesiuk.habit_tracker.repositories.HabitEntryRepository;
import com.arsenfesiuk.habit_tracker.repositories.HabitRepository;
import com.arsenfesiuk.habit_tracker.services.impl.StatsServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StatsServiceTest {

    private HabitRepository habitRepository;
    private HabitEntryRepository habitEntryRepository;
    private StatsServiceJPA service;

    @BeforeEach
    void setUp() {
        habitRepository = mock(HabitRepository.class);
        habitEntryRepository = mock(HabitEntryRepository.class);
        service = new StatsServiceJPA(habitRepository, habitEntryRepository);

        User user = new User();
        user.setId(1L);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }

    @Test
    void completionRate_allDone() {
        Habit h = habitWithId(10L);
        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to   = LocalDate.of(2026, 5, 7); // 7 days, 1 habit → 7 slots

        List<HabitEntry> entries = List.of(
                entryFor(h, LocalDate.of(2026, 5, 1)),
                entryFor(h, LocalDate.of(2026, 5, 2)),
                entryFor(h, LocalDate.of(2026, 5, 3)),
                entryFor(h, LocalDate.of(2026, 5, 4)),
                entryFor(h, LocalDate.of(2026, 5, 5)),
                entryFor(h, LocalDate.of(2026, 5, 6)),
                entryFor(h, LocalDate.of(2026, 5, 7))
        );

        when(habitRepository.findAllByUserId(1L)).thenReturn(List.of(h));
        when(habitEntryRepository.findByHabitInAndDateBetween(List.of(h), from, to))
                .thenReturn(entries);

        StatsDTO stats = service.getStats(from, to);

        assertThat(stats.getCompletionRate()).isEqualTo(100);
        assertThat(stats.getTotalCompletions()).isEqualTo(7);
    }

    @Test
    void completionRate_halfDone() {
        Habit h = habitWithId(10L);
        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to   = LocalDate.of(2026, 5, 4); // 4 days, 1 habit → 4 slots, 2 done

        when(habitRepository.findAllByUserId(1L)).thenReturn(List.of(h));
        when(habitEntryRepository.findByHabitInAndDateBetween(List.of(h), from, to))
                .thenReturn(List.of(
                        entryFor(h, LocalDate.of(2026, 5, 1)),
                        entryFor(h, LocalDate.of(2026, 5, 2))
                ));

        StatsDTO stats = service.getStats(from, to);

        assertThat(stats.getCompletionRate()).isEqualTo(50);
    }

    @Test
    void emptyHabits_returnsZeroStats() {
        when(habitRepository.findAllByUserId(1L)).thenReturn(List.of());

        StatsDTO stats = service.getStats(
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 30));

        assertThat(stats.getCompletionRate()).isZero();
        assertThat(stats.getWeeklyTrend()).isEmpty();
        assertThat(stats.getHabitBreakdown()).isEmpty();
    }

    @Test
    void habitBreakdown_ratePerHabit() {
        Habit h1 = habitWithId(1L); h1.setName("Reading");  h1.setColor("#FF6B6B");
        Habit h2 = habitWithId(2L); h2.setName("Exercise"); h2.setColor("#4ECDC4");

        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to   = LocalDate.of(2026, 5, 4); // 4 days

        // h1: 4/4 done, h2: 0 done
        when(habitRepository.findAllByUserId(1L)).thenReturn(List.of(h1, h2));
        when(habitEntryRepository.findByHabitInAndDateBetween(List.of(h1, h2), from, to))
                .thenReturn(List.of(
                        entryFor(h1, LocalDate.of(2026, 5, 1)),
                        entryFor(h1, LocalDate.of(2026, 5, 2)),
                        entryFor(h1, LocalDate.of(2026, 5, 3)),
                        entryFor(h1, LocalDate.of(2026, 5, 4))
                ));

        StatsDTO stats = service.getStats(from, to);

        assertThat(stats.getHabitBreakdown()).hasSize(2);
        assertThat(stats.getHabitBreakdown().get(0).getRate()).isEqualTo(100);
        assertThat(stats.getHabitBreakdown().get(1).getRate()).isEqualTo(0);
    }

    @Test
    void weeklyTrend_singleFullWeek() {
        Habit h = habitWithId(1L);
        // Week: Mon 2026-05-11 → Sun 2026-05-17
        LocalDate from = LocalDate.of(2026, 5, 11); // Monday
        LocalDate to   = LocalDate.of(2026, 5, 17); // Sunday (7 days)

        // 4 done out of 7 → 57%
        when(habitRepository.findAllByUserId(1L)).thenReturn(List.of(h));
        when(habitEntryRepository.findByHabitInAndDateBetween(List.of(h), from, to))
                .thenReturn(List.of(
                        entryFor(h, LocalDate.of(2026, 5, 11)),
                        entryFor(h, LocalDate.of(2026, 5, 12)),
                        entryFor(h, LocalDate.of(2026, 5, 13)),
                        entryFor(h, LocalDate.of(2026, 5, 14))
                ));

        StatsDTO stats = service.getStats(from, to);

        assertThat(stats.getWeeklyTrend()).hasSize(1);
        assertThat(stats.getWeeklyTrend().get(0).getRate()).isEqualTo(57);
    }

    // — helpers —

    private Habit habitWithId(Long id) {
        Habit h = new Habit();
        h.setId(id);
        h.setName("Habit " + id);
        h.setColor("#ffffff");
        return h;
    }

    private HabitEntry entryFor(Habit habit, LocalDate date) {
        HabitEntry e = new HabitEntry();
        e.setHabit(habit);
        e.setDate(date);
        e.setCount(1);
        return e;
    }
}
