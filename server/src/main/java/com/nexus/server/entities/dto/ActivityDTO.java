package com.nexus.server.entities.dto;

import com.nexus.server.entities.ActivityType;

import java.time.LocalDate;

public class ActivityDTO {
    private Long id;
    private String title;
    private String description;
    private ActivityType type;
    private LocalDate createdAt;
    private String percentage;
    private UserDTO user;

    public ActivityDTO(Long id, String title, String description, ActivityType type, LocalDate createdAt, String percentage, UserDTO user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.createdAt = createdAt;
        this.percentage = percentage;
        this.user = user;
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

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
