package com.arsenfesiuk.habit_tracker.mappers;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitDTO;
import org.springframework.stereotype.Component;

@Component
public class HabitMapper {
    public HabitDTO toDTO(Habit habit) {
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setId(habit.getId());
        habitDTO.setName(habit.getName());
        habitDTO.setCount(habit.getCount());
        return habitDTO;
    }
}
