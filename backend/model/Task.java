package com.todo.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private String id;
    private String title;
    private String description;
    private int durationMinutes;
    private boolean completed;

    // Constructors
    public Task() {}

    public Task(String id, String title, String description, int durationMinutes, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.completed = completed;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

@Entity
@Table(name = "task_store")
public class TaskStore {
    @Id
    private Long id = 1L; // Single row approach

    @Lob
    @Column(columnDefinition = "TEXT")
    private String tasksJson; // The entire List<Task> stored as JSON string

    // Constructors
    public TaskStore() {}

    public TaskStore(String tasksJson) {
        this.id = 1L;
        this.tasksJson = tasksJson;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTasksJson() {
        return tasksJson;
    }

    public void setTasksJson(String tasksJson) {
        this.tasksJson = tasksJson;
    }
}