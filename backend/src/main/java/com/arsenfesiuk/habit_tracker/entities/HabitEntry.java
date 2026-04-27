package com.arsenfesiuk.habit_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "habit_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id", "date"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    private LocalDate date;

    private int count;
}

