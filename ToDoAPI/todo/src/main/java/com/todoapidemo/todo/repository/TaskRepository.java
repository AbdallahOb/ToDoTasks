package com.todoapidemo.todo.repository;

import com.todoapidemo.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    
}
