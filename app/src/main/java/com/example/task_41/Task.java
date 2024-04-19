package com.example.task_41;

public class Task {
private int id;
    private String title;
    private String description;
    private long createdAt;
    private long modifiedAt;

    public Task(int id, String title, String description, long createdAt, long modifiedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
