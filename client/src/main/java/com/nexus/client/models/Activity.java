package com.nexus.client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    private Long id;
    private String title;
    private String description;
    private ActivityType type;
    private LocalDate createdAt;
    private String percentage;
    private User user;

    // Constructor
    public Activity() {}

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public ActivityType getType() { return type; }

    public void setType(ActivityType type) { this.type = type; }

    public LocalDate getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public String getPercentage() { return percentage; }

    public void setPercentage(String percentage) { this.percentage = percentage; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
