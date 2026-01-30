package com.arsenfesiuk.habit_tracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler(HabitNotFoundException.class)
    public ResponseEntity<String> habitNotFoundException(HabitNotFoundException e) {
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
