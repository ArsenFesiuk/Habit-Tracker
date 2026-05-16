package com.arsenfesiuk.habit_tracker.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitBreakdownDTO {
    private String name;
    private String color;
    private int rate;
}
