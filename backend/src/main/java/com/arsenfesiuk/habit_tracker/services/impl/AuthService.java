package com.arsenfesiuk.habit_tracker.services.impl;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.LoginRequest;
import com.arsenfesiuk.habit_tracker.exceptions.UserNotFoundException;
import com.arsenfesiuk.habit_tracker.repositories.UserRepository;
import com.arsenfesiuk.habit_tracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
    }
}
