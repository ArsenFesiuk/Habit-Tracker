package com.arsenfesiuk.habit_tracker.services;

import com.arsenfesiuk.habit_tracker.entities.dto.HabitDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitDayDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateHabitRequest;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.update.UpdateHabitRequest;

import java.time.LocalDate;
import java.util.List;

public interface HabitService {
    List<HabitDTO> getAll();
    List<HabitDTO> getMyHabits();
    HabitDTO create(CreateHabitRequest request);
    HabitDTO update(Long id, UpdateHabitRequest request);
    void delete(Long id);

    HabitDTO mark(Long id, LocalDate date);
    HabitDTO unmark(Long id, LocalDate date);
    List<HabitDayDTO> getEntries(Long id, LocalDate from, LocalDate to);
}
