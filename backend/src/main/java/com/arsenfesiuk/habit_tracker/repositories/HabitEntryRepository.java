package com.arsenfesiuk.habit_tracker.repositories;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.HabitEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitEntryRepository extends JpaRepository<HabitEntry, Long> {
    Optional<HabitEntry> findByHabitAndDate(Habit habit, LocalDate date);

    List<HabitEntry> findByHabitAndDateBetween(
            Habit habit,
            LocalDate start,
            LocalDate end
    );
}
