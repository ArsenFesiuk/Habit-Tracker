package com.arsenfesiuk.habit_tracker.entities.dto;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Role role;
}
