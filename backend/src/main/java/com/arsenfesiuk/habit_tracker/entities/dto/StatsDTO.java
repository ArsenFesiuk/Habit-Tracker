package com.arsenfesiuk.habit_tracker.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
    private int completionRate;
    private int totalCompletions;
    private int activeHabits;
    private int bestCurrentStreak;
    private List<WeeklyTrendPointDTO> weeklyTrend;
    private List<HabitBreakdownDTO> habitBreakdown;
}
