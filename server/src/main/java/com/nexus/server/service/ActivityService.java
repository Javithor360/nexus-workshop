package com.nexus.server.service;

import com.nexus.server.entities.ActivityType;
import com.nexus.server.repositories.IActivityTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final IActivityTypeRepository activityTypeRepository;

    @Autowired
    public ActivityService(IActivityTypeRepository activityTypeRepository) {
        this.activityTypeRepository = activityTypeRepository;
    }

    /**
     * Get all activity types
     * @return List of all activity types
     */
    public List<ActivityType> getAllActivityTypes() {
        return activityTypeRepository.findAll();
    }

    /**
     * Get activity type by id
     * @param id Activity type id
     * @return Activity type
     */
    public Optional<ActivityType> getActivityTypeById(Long id) {
        return activityTypeRepository.findById(id);
    }

    /**
     * Create activity type
     * @param activityType Activity type
     * @return Activity type
     */
    public ActivityType createActivityType(ActivityType activityType) {
        return activityTypeRepository.save(activityType);
    }

    /**
     * Update activity type
     * @param id Activity type id
     * @param activityTypeDetails Activity type details
     * @return Activity type
     */
    public Optional<ActivityType> updateActivityType(Long id, ActivityType activityTypeDetails) {
        return activityTypeRepository.findById(id)
                .map(activityType -> {
                    activityType.setName(activityTypeDetails.getName());
                    return activityTypeRepository.save(activityType);
                });
    }

    /**
     * Delete activity type
     * @param id Activity type id
     */
    public void deleteActivityType(Long id) {
        activityTypeRepository.deleteById(id);
    }
}
