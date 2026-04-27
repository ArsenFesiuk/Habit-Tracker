package com.arsenfesiuk.habit_tracker.services.impl;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.HabitEntry;
import com.arsenfesiuk.habit_tracker.repositories.HabitEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitEntryServiceJPA {

    private final HabitEntryRepository habitEntryRepository;

    @Transactional
    public void setDone(Habit habit, LocalDate date, boolean done) {
        var existing = habitEntryRepository.findByHabitAndDate(habit, date);
        if (done) {
            HabitEntry entry = existing.orElseGet(() -> {
                HabitEntry e = new HabitEntry();
                e.setHabit(habit);
                e.setDate(date);
                return e;
            });
            entry.setCount(1);
            habitEntryRepository.save(entry);
        } else {
            existing.ifPresent(habitEntryRepository::delete);
        }
    }

    @Transactional(readOnly = true)
    public List<HabitEntry> getEntries(Habit habit, LocalDate from, LocalDate to) {
        return habitEntryRepository.findByHabitAndDateBetween(habit, from, to);
    }
}
