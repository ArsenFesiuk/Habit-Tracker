package com.arsenfesiuk.habit_tracker.repositories;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    Optional<Habit> findByName(String name);
    List<Habit> findAllByUserId(Long userId);
    Habit findByIdAndUser(Long id, User user);
}
