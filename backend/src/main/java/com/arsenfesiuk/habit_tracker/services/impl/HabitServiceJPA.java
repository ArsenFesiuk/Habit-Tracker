package com.arsenfesiuk.habit_tracker.services.impl;

import com.arsenfesiuk.habit_tracker.entities.Habit;
import com.arsenfesiuk.habit_tracker.entities.User;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.HabitDayDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateHabitRequest;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.update.UpdateHabitRequest;
import com.arsenfesiuk.habit_tracker.exceptions.BadRequestException;
import com.arsenfesiuk.habit_tracker.factories.HabitFactory;
import com.arsenfesiuk.habit_tracker.mappers.HabitMapper;
import com.arsenfesiuk.habit_tracker.repositories.HabitRepository;
import com.arsenfesiuk.habit_tracker.services.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitServiceJPA implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;
    private final HabitFactory habitFactory;
    private final HabitEntryServiceJPA habitEntryService;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    private LocalDate weekStart() {
        return LocalDate.now().minusDays(6);
    }

    private LocalDate today() {
        return LocalDate.now();
    }

    @Override
    public List<HabitDTO> getAll() {
        LocalDate from = weekStart();
        LocalDate to = today();
        return habitRepository.findAll().stream()
                .map(h -> habitMapper.toDTO(h, from, to))
                .toList();
    }

    @Override
    public List<HabitDTO> getMyHabits() {
        User user = getCurrentUser();
        LocalDate from = weekStart();
        LocalDate to = today();
        return habitRepository.findAllByUserId(user.getId()).stream()
                .map(h -> habitMapper.toDTO(h, from, to))
                .toList();
    }

    @Override
    public HabitDTO create(CreateHabitRequest request) {
        User user = getCurrentUser();
        Habit habit = habitFactory.create(request, user);
        Habit saved = habitRepository.save(habit);
        return habitMapper.toDTO(saved, weekStart(), today());
    }

    @Override
    public HabitDTO update(Long id, UpdateHabitRequest request) {
        User user = getCurrentUser();
        Habit habit = habitRepository.findByIdAndUser(id, user);
        if (request.getName() != null) {
            habit.setName(request.getName());
        }
        Habit saved = habitRepository.save(habit);
        return habitMapper.toDTO(saved, weekStart(), today());
    }

    @Override
    public void delete(Long id) {
        User user = getCurrentUser();
        Habit habit = habitRepository.findByIdAndUser(id, user);
        habitRepository.delete(habit);
    }

    @Override
    public HabitDTO mark(Long id, LocalDate date) {
        return setDone(id, date, true);
    }

    @Override
    public HabitDTO unmark(Long id, LocalDate date) {
        return setDone(id, date, false);
    }

    private HabitDTO setDone(Long id, LocalDate date, boolean done) {
        validateEditable(date);
        User user = getCurrentUser();
        Habit habit = habitRepository.findByIdAndUser(id, user);
        habitEntryService.setDone(habit, date, done);
        return habitMapper.toDTO(habit, weekStart(), today());
    }

    private void validateEditable(LocalDate date) {
        LocalDate from = weekStart();
        LocalDate to = today();
        if (date.isBefore(from) || date.isAfter(to)) {
            throw new BadRequestException("Date out of editable range");
        }
    }

    @Override
    public List<HabitDayDTO> getEntries(Long id, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BadRequestException("`from` must be on or before `to`");
        }
        User user = getCurrentUser();
        Habit habit = habitRepository.findByIdAndUser(id, user);
        return habitMapper.toDays(habit, from, to);
    }
}
