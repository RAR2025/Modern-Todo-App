package com.todo.backend.controller;

import com.todo.backend.model.Task;
import com.todo.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    private final TaskService taskService;
    
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    /**
     * GET /api/tasks - Retrieve all tasks
     */
    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * POST /api/tasks - Add a new task
     */
    @PostMapping("/")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        Task createdTask = taskService.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    
    /**
     * DELETE /api/tasks/{id} - Delete a task by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * PATCH /api/tasks/{id}/toggle - Toggle task completion status
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleComplete(@PathVariable String id) {
        Task updatedTask = taskService.toggleComplete(id);
        return ResponseEntity.ok(updatedTask);
    }
}
