package com.todo.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class TaskStore {

    @Id
    private Long id = 1L;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String tasksJson;

    public TaskStore() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTasksJson() { return tasksJson; }
    public void setTasksJson(String tasksJson) { this.tasksJson = tasksJson; }
}
