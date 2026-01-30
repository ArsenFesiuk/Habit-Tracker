package com.arsenfesiuk.habit_tracker.controllers;

import com.arsenfesiuk.habit_tracker.entities.dto.UserDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateUserRequest;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.update.UpdateUserRequest;
import com.arsenfesiuk.habit_tracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        userService.delete(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

}
