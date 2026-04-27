package com.arsenfesiuk.habit_tracker.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitDayDTO {
    private LocalDate date;
    private boolean done;
}
