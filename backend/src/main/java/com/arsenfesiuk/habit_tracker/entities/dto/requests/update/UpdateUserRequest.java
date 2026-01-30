package com.arsenfesiuk.habit_tracker.entities.dto.requests.update;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @Nullable private String name;
    @Nullable private String surname;
    @Nullable private String email;

}
