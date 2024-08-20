package com.todoapidemo.todo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
public class Task {
    ////////////////// Define fields //////////////////
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "task_description")
    private String description;
    @Column(name = "task_status")
    private boolean status;
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    ////////////////// Define constructors //////////////////
    public Task(){}
    public Task(String title, String description, boolean status, LocalDateTime dueDate, LocalDateTime creationDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.creationDate = creationDate;
    }
    ////////////////// Define getters/setters //////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    ////////////////// @Override toString //////////////////
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dueDate=" + dueDate +
                ", creationDate=" + creationDate +
                '}';
    }
}
