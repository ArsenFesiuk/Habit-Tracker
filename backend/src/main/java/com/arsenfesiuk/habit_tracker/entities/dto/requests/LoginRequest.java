package com.arsenfesiuk.habit_tracker.entities.dto.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
