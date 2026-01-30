package com.arsenfesiuk.habit_tracker.services.impl;

import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.UserDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateUserRequest;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.update.UpdateUserRequest;
import com.arsenfesiuk.habit_tracker.exceptions.UserNotFoundException;
import com.arsenfesiuk.habit_tracker.factories.UserFactory;
import com.arsenfesiuk.habit_tracker.mappers.UserMapper;
import com.arsenfesiuk.habit_tracker.repositories.UserRepository;
import com.arsenfesiuk.habit_tracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceJPA implements UserService {

    private final UserRepository userRepository;
    private final UserFactory factory;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + email));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO create(CreateUserRequest request) {
        User user = factory.create(request);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO update(Long id, UpdateUserRequest request) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getSurname() != null) {
            user.setSurname(request.getSurname());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
