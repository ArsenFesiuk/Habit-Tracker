package com.arsenfesiuk.habit_tracker.entities.dto.requests.update;

import com.arsenfesiuk.habit_tracker.entities.User;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateHabitRequest {
    private String name;
    private long count;
    private List<LocalDate> completedDays;
}
