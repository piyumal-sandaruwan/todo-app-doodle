package com.piyumal.todo_app_doodle;

public class Task {
    private String taskId;
    private String title;
    private String description;
    private String scheduledDate; // Format: "2026-05-15"
    private String scheduledTime; // Format: "10:30 AM"
    private boolean isCompleted;
    private long createdAt;

    public Task() {}

    public Task(String taskId, String title, String description, String scheduledDate, String scheduledTime, long createdAt) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.isCompleted = false;
        this.createdAt = createdAt;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getScheduledDate() { return scheduledDate; }
    public String getScheduledTime() { return scheduledTime; }
    public boolean isCompleted() { return isCompleted; }
    public long getCreatedAt() { return createdAt; }

    // Setters
    public void setCompleted(boolean completed) { isCompleted = completed; }
}