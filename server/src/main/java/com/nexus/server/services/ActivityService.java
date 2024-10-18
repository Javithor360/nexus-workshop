package com.nexus.server.services;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.Log;
import com.nexus.server.entities.Project;
import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.repositories.IActivityRepository;
import com.nexus.server.repositories.ILogRepository;
import com.nexus.server.repositories.IProjectRepository;
import com.nexus.server.services.extra.DTOConverter;
import com.nexus.server.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final IActivityRepository activityRepository;
    private final ILogRepository logRepository;
    private final IProjectRepository projectRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public ActivityService(IActivityRepository activityRepository, ILogRepository logRepository, IProjectRepository projectRepository, DTOConverter dtoConverter) {
        this.activityRepository = activityRepository;
        this.logRepository = logRepository;
        this.projectRepository = projectRepository;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Get all activities
     *
     * @return List of all activity types
     */
    public List<ActivityDTO> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get activity type by id
     *
     * @param id Activity type id
     * @return Activity type
     */
    public Optional<ActivityDTO> getActivityById(Long id) {
        return activityRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Get all activities that match the user id
     *
     * @param userId the user id
     * @return List of all activities related to the user
     */
    public Optional<List<ActivityDTO>> getAllActivitiesByUserId(Long userId) {
        List<Activity> activities = activityRepository.findAllByUserId(userId);
        return Optional.of(activities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    /**
     * Get all activities that match the project id
     *
     * @param projectId the project id
     * @return List of all activities related to the project
     */
    public Optional<List<ActivityDTO>> getAllActivitiesByProjectId(Long projectId) {
        Optional<List<Log>> logsOptional = logRepository.findByProjectId(projectId);
        if (logsOptional.isEmpty()) {
            return Optional.empty();
        }
        List<Log> logs = logsOptional.get();
        List<Activity> activities = logs.stream()
                .map(Log::getActivity)
                .distinct()
                .toList();
        return Optional.of(activities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }

    /**
     * Create activity type
     *
     * @param activityType Activity type
     * @return Activity type
     */
    public Activity createActivity(Activity activityType, Long projectId) {
        Activity savedActivity = activityRepository.save(activityType);

        // Creating the new log entry
        Log log = new Log();
        log.setActivity(savedActivity);

        // Fetch the project entity
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id '" + projectId + "' not found"));
        log.setProject(project);

        // Save the log entry
        logRepository.save(log);

        return savedActivity;
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
