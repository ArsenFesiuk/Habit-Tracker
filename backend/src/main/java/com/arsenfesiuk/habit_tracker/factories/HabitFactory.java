package com.arsenfesiuk.habit_tracker.factories;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateHabitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HabitFactory {
    public Habit create(CreateHabitRequest request, User user) {
        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setCount(request.getCount());
        habit.setUser(user);
        return habit;
    }
}
