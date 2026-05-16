package com.arsenfesiuk.habit_tracker.services;

import com.arsenfesiuk.habit_tracker.entities.dto.StatsDTO;

import java.time.LocalDate;

public interface StatsService {
    StatsDTO getStats(LocalDate from, LocalDate to);
}
