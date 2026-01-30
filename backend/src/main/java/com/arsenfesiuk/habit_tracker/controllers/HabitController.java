package com.arsenfesiuk.habit_tracker.controllers;

import com.arsenfesiuk.habit_tracker.entities.dto.HabitDTO;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.create.CreateHabitRequest;
import com.arsenfesiuk.habit_tracker.entities.dto.requests.update.UpdateHabitRequest;
import com.arsenfesiuk.habit_tracker.services.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {
    private final HabitService habitService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HabitDTO>> getAll()
    {
        return  ResponseEntity.ok(habitService.getAll());
    }
    @GetMapping("/me")
    public ResponseEntity<List<HabitDTO>> getMyHabits()
    {
        return  ResponseEntity.ok(habitService.getMyHabits());
    }

    @PostMapping
    public ResponseEntity<HabitDTO> create(@RequestBody CreateHabitRequest request)
    {
        return ResponseEntity.ok(habitService.create(request));
    }
    @PatchMapping
    public ResponseEntity<HabitDTO> update(
            @PathVariable Long id,
            @RequestBody UpdateHabitRequest request
    ){
        return ResponseEntity.ok(habitService.update(id, request));
    }
    @DeleteMapping("/{id}")
    public void  delete(@PathVariable Long id)
    {
        habitService.delete(id);
    }

}
