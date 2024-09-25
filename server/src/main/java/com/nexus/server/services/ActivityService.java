package com.nexus.server.services;

import com.nexus.server.entities.Activity;
import com.nexus.server.repositories.IActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final IActivityRepository activityRepository;

    @Autowired
    public ActivityService(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Get all activity types
     * @return List of all activity types
     */
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    /**
     * Get activity type by id
     * @param id Activity type id
     * @return Activity type
     */
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    /**
     * Create activity type
     * @param activityType Activity type
     * @return Activity type
     */
    public Activity createActivityType(Activity activityType) {
        return activityRepository.save(activityType);
    }

    /**
     * Update activity type
     * @param id Activity type id
     * @param activityDetails Activity type details
     * @return Activity type
     */
    public Optional<Activity> updateActivityType(Long id, Activity activityDetails) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activity.setDescription(activityDetails.getDescription());

                    return activityRepository.save(activity);
                });
    }

    /**
     * Delete activity type
     * @param id Activity type id
     */
    public void deleteActivityType(Long id) {
        activityRepository.deleteById(id);
    }
}
