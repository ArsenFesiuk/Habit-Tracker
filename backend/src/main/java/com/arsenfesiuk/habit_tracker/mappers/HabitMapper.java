package com.arsenfesiuk.habit_tracker.mappers;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.HabitEntry;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitDayDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HabitMapper {

    public HabitDTO toDTO(Habit habit, LocalDate from, LocalDate to) {
        List<HabitDayDTO> days = toDays(habit, from, to);

        int totalCount = (int) habit.getEntries().stream()
                .filter(e -> e.getCount() > 0)
                .count();

        HabitDTO dto = new HabitDTO();
        dto.setId(habit.getId());
        dto.setName(habit.getName());
        dto.setTotalCount(totalCount);
        dto.setDays(days);
        return dto;
    }

    public List<HabitDayDTO> toDays(Habit habit, LocalDate from, LocalDate to) {
        Map<LocalDate, Boolean> doneByDate = habit.getEntries().stream()
                .filter(e -> e.getCount() > 0)
                .collect(Collectors.toMap(HabitEntry::getDate, e -> true, (a, b) -> a));

        List<HabitDayDTO> days = new ArrayList<>();
        LocalDate cursor = from;
        while (!cursor.isAfter(to)) {
            days.add(new HabitDayDTO(cursor, doneByDate.getOrDefault(cursor, false)));
            cursor = cursor.plusDays(1);
        }
        return days;
    }
}
