package com.nexus.server.controllers;

import com.nexus.server.entities.Activity;
import com.nexus.server.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class ActivityController {
    private final ActivityService activityService;

    // @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

}
