package com.arsenfesiuk.habit_tracker.entities.dto.requests.create;

import com.arsenfesiuk.habit_tracker.entities.Role;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @Nullable private String name;
    @Nullable private String surname;
    private String email;
    private String password;
    private Role role;
}
