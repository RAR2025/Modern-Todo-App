package com.todo.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.todo.backend.model.Task;
import com.todo.backend.model.TaskStore;
import com.todo.backend.repository.TaskStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class TaskService {
    
    private final TaskStoreRepository taskStoreRepository;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public TaskService(TaskStoreRepository taskStoreRepository, ObjectMapper objectMapper) {
        this.taskStoreRepository = taskStoreRepository;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Retrieve all tasks from the TaskStore entity
     */
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        try {
            Optional<TaskStore> taskStore = taskStoreRepository.findById(1L);
            
            if (taskStore.isPresent() && taskStore.get().getTasksJson() != null && !taskStore.get().getTasksJson().isEmpty()) {
                return objectMapper.readValue(taskStore.get().getTasksJson(), new TypeReference<List<Task>>() {});
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing tasks", e);
        }
    }
    
    /**
     * Add a new task with auto-generated UUID
     */
    @Transactional
    public Task addTask(Task task) {
        try {
            // Auto-generate UUID for the task
            task.setId(UUID.randomUUID().toString());
            
            // Get existing tasks
            List<Task> tasks = getAllTasks();
            
            // Add the new task
            tasks.add(task);
            
            // Serialize and save
            String tasksJson = objectMapper.writeValueAsString(tasks);
            TaskStore taskStore = taskStoreRepository.findById(1L)
                    .orElse(new TaskStore());
            taskStore.setId(1L);
            taskStore.setTasksJson(tasksJson);
            taskStoreRepository.save(taskStore);
            
            return task;
        } catch (Exception e) {
            throw new RuntimeException("Error adding task", e);
        }
    }
    
    /**
     * Delete a task by its ID
     */
    @Transactional
    public void deleteTask(String id) {
        try {
            List<Task> tasks = getAllTasks();
            
            // Remove the task with the given ID
            tasks.removeIf(task -> task.getId().equals(id));
            
            // Serialize and save
            String tasksJson = objectMapper.writeValueAsString(tasks);
            TaskStore taskStore = taskStoreRepository.findById(1L)
                    .orElse(new TaskStore());
            taskStore.setId(1L);
            taskStore.setTasksJson(tasksJson);
            taskStoreRepository.save(taskStore);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task", e);
        }
    }
    
    /**
     * Toggle the completion status of a task by its ID
     */
    @Transactional
    public Task toggleComplete(String id) {
        try {
            List<Task> tasks = getAllTasks();
            
            Task targetTask = null;
            for (Task task : tasks) {
                if (task.getId().equals(id)) {
                    task.setCompleted(!task.isCompleted());
                    targetTask = task;
                    break;
                }
            }
            
            if (targetTask == null) {
                throw new RuntimeException("Task not found with id: " + id);
            }
            
            // Serialize and save
            String tasksJson = objectMapper.writeValueAsString(tasks);
            TaskStore taskStore = taskStoreRepository.findById(1L)
                    .orElse(new TaskStore());
            taskStore.setId(1L);
            taskStore.setTasksJson(tasksJson);
            taskStoreRepository.save(taskStore);
            
            return targetTask;
        } catch (Exception e) {
            throw new RuntimeException("Error toggling task completion status", e);
        }
    }
}
