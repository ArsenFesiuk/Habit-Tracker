package com.arsenfesiuk.habit_tracker.entities.dto.requests.update;

import com.arsenfesiuk.habit_tracker.entities.User;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateHabitRequest {
    @Nullable
    private String name;
    @Nullable
    private int count;
}
