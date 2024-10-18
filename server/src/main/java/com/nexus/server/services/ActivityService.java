package com.nexus.server.services;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.repositories.IActivityRepository;
import com.nexus.server.services.extra.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final IActivityRepository activityRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public ActivityService(IActivityRepository activityRepository, DTOConverter dtoConverter) {
        this.activityRepository = activityRepository;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Get all activity types
     *
     * @return List of all activity types
     */
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    /**
     * Get activity type by id
     *
     * @param id Activity type id
     * @return Activity type
     */
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    /**
     * Create activity type
     *
     * @param activityType Activity type
     * @return Activity type
     */
    public Activity createActivity(Activity activityType) {
        return activityRepository.save(activityType);
    }

    /**
     * Update activity type
     *
     * @param id              Activity type id
     * @param activityDetails Activity type details
     * @return Activity type
     */
    public Optional<Activity> updateActivity(Long id, Activity activityDetails) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activity.setDescription(activityDetails.getDescription());

                    return activityRepository.save(activity);
                });
    }

    /**
     * Delete activity type
     *
     * @param id Activity type id
     */
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    // Helper method to convert Activity to DTO
    public ActivityDTO convertToDTO(Activity activity) {
        return dtoConverter.convertToDTO(activity, dtoConverter.convertToDTO(activity.getUser()));
    }
}
