package com.arsenfesiuk.habit_tracker.entities.dto.requests.create;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateHabitRequest {
    @Nullable
    private String name;
}
