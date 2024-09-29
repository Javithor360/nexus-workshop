package com.nexus.server.controllers;

import com.nexus.server.entities.Activity;
import com.nexus.server.services.ActivityService;
import com.nexus.server.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities") // http://localhost:8081/api/activities
public class ActivityController {
    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * Get all activities
     *
     * @return List of all activities
     * @route GET /api/activities
     */
    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

    /**
     * Get activity by id
     *
     * @param id Activity id
     * @return Activity
     * @route GET /api/activities/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity with id '" + id + "' not found")));
    }



}
