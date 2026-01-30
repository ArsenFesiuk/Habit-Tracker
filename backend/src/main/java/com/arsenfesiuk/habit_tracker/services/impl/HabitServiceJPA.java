package com.arsenfesiuk.habit_tracker.services.impl;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateHabitRequest;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.update.UpdateHabitRequest;
import com.arsenfesiuk.habit_tracker.exceptions.HabitNotFoundException;
import com.arsenfesiuk.habit_tracker.factories.HabitFactory;
import com.arsenfesiuk.habit_tracker.mappers.HabitMapper;
import com.arsenfesiuk.habit_tracker.repositories.HabitRepository;
import com.arsenfesiuk.habit_tracker.services.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class HabitServiceJPA implements HabitService {
    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;
    private final HabitFactory habitFactory;
    @Override
    public List<HabitDTO> getAll() {
        return habitRepository.findAll()
                .stream()
                .map(habitMapper::toDTO)
                .toList();
    }

    @Override
    public List<HabitDTO> getMyHabits() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return habitRepository.findAllByUserId(user.getId())
                .stream()
                .map(habitMapper::toDTO)
                .toList();
    }

    @Override
    public HabitDTO create(CreateHabitRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Habit habit = habitFactory.create(request, user);
        Habit savedHabit = habitRepository.save(habit);
        return habitMapper.toDTO(savedHabit);
    }

    @Override
    public HabitDTO update(Long id, UpdateHabitRequest request) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id " + id));

        if(request.getName() != null) {
            habit.setName(request.getName());
        }

        if(request.getCount() != 0){
            habit.setCount(request.getCount());
        }

        return habitMapper.toDTO(habitRepository.save(habit));
    }

    @Override
    public void delete(Long id) {
        habitRepository.deleteById(id);
    }
}
