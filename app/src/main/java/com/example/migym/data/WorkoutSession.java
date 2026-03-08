package com.example.migym.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Locale;

@Entity(tableName = "workouts")
public class WorkoutSession {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String type;
    private String description;
    private String location;
    private int duration;
    private int dayOfWeek;
    private int startHour;
    private int startMinute;
    private boolean isRecurring;

    public WorkoutSession(String title, String type, String description, String location, int duration, int dayOfWeek, int startHour, int startMinute, boolean isRecurring) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.isRecurring = isRecurring;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getTime() {
        return String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutSession that = (WorkoutSession) o;
        return id == that.id &&
               duration == that.duration &&
               dayOfWeek == that.dayOfWeek &&
               startHour == that.startHour &&
               startMinute == that.startMinute &&
               isRecurring == that.isRecurring &&
               title.equals(that.title) &&
               type.equals(that.type) &&
               description.equals(that.description) &&
               location.equals(that.location);
    }
} 