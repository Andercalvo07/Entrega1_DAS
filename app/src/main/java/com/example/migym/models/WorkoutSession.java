package com.example.migym.models;

public class WorkoutSession {
    private String id;
    private String title;
    private String description;
    private int dayOfWeek;
    private String time;
    private String location;
    private String type;
    private int duration;

    public WorkoutSession() {
        // Constructor vacío requerido
    }

    public WorkoutSession(String id, String title, String description, int dayOfWeek, String time, String location, String type, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.location = location;
        this.type = type;
        this.duration = duration;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDayOfWeek() { return dayOfWeek; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public int getDuration() { return duration; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setTime(String time) { this.time = time; }
    public void setLocation(String location) { this.location = location; }
    public void setType(String type) { this.type = type; }
    public void setDuration(int duration) { this.duration = duration; }
} 