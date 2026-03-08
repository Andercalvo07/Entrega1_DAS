package com.example.migym.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.Date;
import java.util.Objects;
import java.util.Calendar;

@Entity(tableName = "workouts")
@TypeConverters({Converters.class})
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String title;
    private String description;
    private String location;
    private int dayOfWeek;
    private int type;
    private String time;
    private int duration;

    public Workout() {
        // Required by Room
    }

    @androidx.room.Ignore
    public Workout(Long id, String title, String description, String location, int dayOfWeek, int type, String time, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dayOfWeek = dayOfWeek;
        this.type = type;
        this.time = time;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek.toLowerCase()) {
            case "monday":
            case "lunes":
                this.dayOfWeek = Calendar.MONDAY;
                break;
            case "tuesday":
            case "martes":
                this.dayOfWeek = Calendar.TUESDAY;
                break;
            case "wednesday":
            case "miércoles":
                this.dayOfWeek = Calendar.WEDNESDAY;
                break;
            case "thursday":
            case "jueves":
                this.dayOfWeek = Calendar.THURSDAY;
                break;
            case "friday":
            case "viernes":
                this.dayOfWeek = Calendar.FRIDAY;
                break;
            case "saturday":
            case "sábado":
                this.dayOfWeek = Calendar.SATURDAY;
                break;
            case "sunday":
            case "domingo":
                this.dayOfWeek = Calendar.SUNDAY;
                break;
            default:
                this.dayOfWeek = Calendar.MONDAY;
                break;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workout workout = (Workout) o;

        if (dayOfWeek != workout.dayOfWeek) return false;
        if (type != workout.type) return false;
        if (duration != workout.duration) return false;
        if (id != null ? !id.equals(workout.id) : workout.id != null) return false;
        if (!title.equals(workout.title)) return false;
        if (!description.equals(workout.description)) return false;
        if (!location.equals(workout.location)) return false;
        return time.equals(workout.time);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + dayOfWeek;
        result = 31 * result + type;
        result = 31 * result + time.hashCode();
        result = 31 * result + duration;
        return result;
    }
} 