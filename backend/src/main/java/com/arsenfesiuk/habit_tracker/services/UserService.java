package com.arsenfesiuk.habit_tracker.services;

import com.arsenfesiuk.habit_tracker.entities.dto.UserDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateUserRequest;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.update.UpdateUserRequest;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO findByEmail(String email);
    UserDTO create(CreateUserRequest request);
    UserDTO update(Long id, UpdateUserRequest request);
    void delete(Long id);

}
