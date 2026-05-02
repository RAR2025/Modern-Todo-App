package com.todo.backend.repository;

import com.todo.backend.model.TaskStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStoreRepository extends JpaRepository<TaskStore, Long> {
}
