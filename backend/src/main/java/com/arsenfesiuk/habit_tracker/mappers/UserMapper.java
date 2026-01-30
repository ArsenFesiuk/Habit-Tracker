package com.arsenfesiuk.habit_tracker.mappers;

import com.arsenfesiuk.habit_tracker.entities.Role;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.UserDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateUserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.USER);
        return user;
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
