package com.todoapidemo.todo.controller;

import com.todoapidemo.todo.entity.Task;
import com.todoapidemo.todo.exception.TaskNotFoundException;
import com.todoapidemo.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskRestController {
    private TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Expose Get("/tasks") that return all the tasks
    @GetMapping("/tasks")
    public List<Task> getAllTask(){
        return taskService.findAll();
    }
    // Expose Get("/tasks/{taskId}") that return a specific tasks
    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable int taskId){
        Task theTask = taskService.findById(taskId);

        if(theTask == null){
            throw new TaskNotFoundException("Task Id not found - "+ taskId);
        }

        return theTask;
    }
    // Expose Post("/tasks") that add new task
    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task theTask) throws Exception {
        theTask.setId(0);

        Task savedTask = taskService.save(theTask);

        if(savedTask == null){
            throw new Exception("Error occur during addition process of the entity "+ theTask);
        }
        return savedTask;
    }
    // Expose Put("/tasks") that that update existing task
    @PutMapping("/tasks")
    public Task updateTask(@RequestBody Task theTask){
        Task updatedTask = taskService.save(theTask);
        return updatedTask;
    }
    // Expose Delete("/tasks/{taskId}") that delete a specific task
    @DeleteMapping("/tasks/{taskId}")
    public Task deleteTask(@PathVariable int taskId){
        Task tempTask = taskService.findById(taskId);
        if(tempTask == null){
            throw new TaskNotFoundException("Task Id not found - "+ taskId);
        }
        taskService.deleteById(taskId);
        return tempTask;
    }

}
