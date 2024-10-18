package com.nexus.server.controllers;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.services.ActivityService;
import com.nexus.server.utils.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<ActivityDTO> getAllActivities() {
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
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity with id '" + id + "' not found")));
    }

    /**
     * Get all activities that match the user id
     *
     * @param userId the user id
     * @return List of all activities related to the user
     * @route GET /api/activities/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityDTO>> getAllActivitiesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(activityService.getAllActivitiesByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Activities with user id '" + userId + "' not found")));
    }

    /**
     * Get all activities that match the project id
     *
     * @param projectId the project id
     * @return List of all activities related to the project
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ActivityDTO>> getAllActivitiesByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(activityService.getAllActivitiesByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Activities with project id '" + projectId + "' not found")));
    }

    /**
     * Create activity - Request body:
     * <pre>
     *     {
     *         "title": "Activity N°",
     *         "description": "This is an activity example",
     *         "percentage": "10%",
     *         "type_id":   1,
     *         "user_id": 1
     *     }
     * </pre>
     *
     * @param activity Activity
     * @return Activity
     * @route POST /api/activities
     */
    @PostMapping
    public ResponseEntity<Activity> createActivity(@Valid @RequestBody Activity activity) {
        return ResponseEntity.ok(activityService.createActivity(activity));
    }

    /**
     * Update activity - Request body:
     * <pre>
     *     {
     *         "description": "Activity description updated"
     *     }
     * </pre>
     *
     * @param id              Activity id
     * @param activityDetails Activity details
     * @return Activity
     * @route PUT /api/activities/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @Valid @RequestBody Activity activityDetails) {
        return ResponseEntity.ok(activityService.updateActivity(id, activityDetails)
                .orElseThrow(() -> new ResourceNotFoundException("Activity with id '" + id + "' not found")));
    }


    /**
     * Delete activity
     *
     * @param id Activity id
     * @return Response message
     * @route DELETE /api/activities/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteActivity(@PathVariable Long id) {
        if (activityService.getActivityById(id).isEmpty()) {
            throw new ResourceNotFoundException("Activity with id '" + id + "' not found");
        }

        activityService.deleteActivity(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Activity deleted successfully");
        return ResponseEntity.ok(response);
    }

}
