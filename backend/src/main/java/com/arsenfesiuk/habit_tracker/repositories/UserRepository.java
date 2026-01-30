package com.arsenfesiuk.habit_tracker.repositories;

import com.arsenfesiuk.habit_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
