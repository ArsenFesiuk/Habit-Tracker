package com.arsenfesiuk.habit_tracker.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyTrendPointDTO {
    private LocalDate weekStart;
    private int rate;
}
