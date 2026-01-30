package com.arsenfesiuk.habit_tracker.config;

import com.arsenfesiuk.habit_tracker.entities.Role;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private void createAdmin() {
        User user = new User();
        user.setName("admin");
        user.setSurname("admin");
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
    private void createUser() {
        User user = new User();
        user.setName("user");
        user.setSurname("user");
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRole(Role.USER);
        userRepository.save(user);
    }
    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() <= 1) {
            createAdmin();
            createUser();
            System.out.println("Admin has been created and user");
        }else{
            System.out.println("Admin and user already exists");
        }
    }
}
