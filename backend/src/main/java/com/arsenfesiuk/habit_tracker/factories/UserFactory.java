package com.arsenfesiuk.habit_tracker.factories;

import com.arsenfesiuk.habit_tracker.entities.Role;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final PasswordEncoder passwordEncoder;
    public User create(CreateUserRequest request) {
         User user = new User();
         user.setName(request.getName());
         user.setSurname(request.getSurname());
         user.setEmail(request.getEmail());
         user.setPassword(passwordEncoder.encode(request.getPassword()));
         user.setRole(Role.USER);
         return user;
    }
}
