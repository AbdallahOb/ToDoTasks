package com.todoapidemo.todo.service;

import com.todoapidemo.todo.entity.Task;
import java.util.List;

public interface TaskService {
    List<Task> findAll();
    Task findById(int theId);
    Task save(Task theTask);
    void deleteById(int theId);



}
