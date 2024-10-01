package com.nexus.server.controllers;

import com.nexus.server.entities.ActivityType;
import com.nexus.server.services.ActivityTypeService;
import com.nexus.server.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activities-types") // http://localhost:8081/api/activities-types
public class ActivityTypeController {
    private final ActivityTypeService activityService;

    @Autowired
    public ActivityTypeController(ActivityTypeService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<ActivityType> getAllActivityTypes() {
        return activityService.getAllActivityTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityType> getActivityTypeById(@PathVariable Long id){
        return ResponseEntity.ok(activityService.getActivityTypeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity type with id '" + id + "' not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityType> updateActivityType(@PathVariable Long id, @RequestBody ActivityType activityTypeDetails){
        return ResponseEntity.ok(activityService.updateActivityType(id, activityTypeDetails)
                .orElseThrow(() -> new ResourceNotFoundException("Activity type with id '" + id + "' not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteActivityType(@PathVariable Long id) {
        if(activityService.getActivityTypeById(id).isEmpty()) {
            throw  new ResourceNotFoundException("Activity type with id '" + id + "' not found");
        }

        activityService.deleteActivityType(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Activity type deleted successfully");
        return ResponseEntity.ok(response);
    }
}
