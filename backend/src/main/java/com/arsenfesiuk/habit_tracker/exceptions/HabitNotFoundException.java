package com.arsenfesiuk.habit_tracker.exceptions;

public class HabitNotFoundException extends RuntimeException {
    public HabitNotFoundException(String message) {
        super(message);
    }
}
