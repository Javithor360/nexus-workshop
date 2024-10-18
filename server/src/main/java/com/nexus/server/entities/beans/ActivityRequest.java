package com.nexus.server.entities.beans;

import com.nexus.server.entities.ActivityType;
import com.nexus.server.entities.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ActivityRequest {
    @NotNull
    private Long projectId;

    @NotNull
    private String title;

    private String description;

    private Double percentage;

    @NotNull
    private ActivityType type;

    @NotNull
    private User user;

    @NotNull
    private LocalDate createdAt;

    public @NotNull Long getProjectId() {
        return projectId;
    }

    public void setProjectId(@NotNull Long projectId) {
        this.projectId = projectId;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public @NotNull ActivityType getType() {
        return type;
    }

    public void setType(@NotNull ActivityType type) {
        this.type = type;
    }

    public @NotNull User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public @NotNull LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
