package com.nexus.server.services.extra;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.Project;
import com.nexus.server.entities.beans.DashboardTools;
import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.entities.dto.ProjectDTO;
import com.nexus.server.entities.dto.UserDTO;
import com.nexus.server.repositories.IActivityRepository;
import com.nexus.server.repositories.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardToolService {

    private final IProjectRepository projectRepository;
    private final IActivityRepository activityRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public DashboardToolService(IProjectRepository projectRepository, IActivityRepository activityRepository, DTOConverter dtoConverter) {
        this.projectRepository = projectRepository;
        this.activityRepository = activityRepository;
        this.dtoConverter = dtoConverter;
    }


    /**
     * Get all the tools for the boss dashboard
     *
     * @return DashboardBossToolsDTO with project count with status in progress, project count with status completed, last top 5 activities and last top 5 projects
     */
    public DashboardTools getDashboardBossTools() {
        int totalInProgressProjects = projectRepository.findByStatusId(2L).size();
        int totalCompletedProjects = projectRepository.findByStatusId(4L).size();
        List<Activity> top5Activities = activityRepository.findTop5AllByOrderByCreatedAtDesc();
        List<Project> top5Projects = projectRepository.findTop5AllByOrderByEndDateDesc();

        List<ActivityDTO> top5ActivitiesDTO = top5Activities.stream()
                .map(activity -> dtoConverter.convertToDTO(activity, dtoConverter.convertToDTO(activity.getUser())))
                .collect(Collectors.toList());

        List<ProjectDTO> top5ProjectsDTO = top5Projects.stream().map(project -> {
            UserDTO userDTO = dtoConverter.convertToDTO(project.getUser());
            return new ProjectDTO(
                    project.getId(),
                    project.getClient(),
                    userDTO,
                    project.getTitle(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getDueDate(),
                    null
            );
        }).collect(Collectors.toList());

        return new DashboardTools(
                totalInProgressProjects,
                totalCompletedProjects,
                top5ActivitiesDTO,
                top5ProjectsDTO
        );
    }

    /**
     * Get all the tools for the employee dashboard
     *
     * @param userId the user id
     * @return DashboardEmployeeToolsDTO with project count with status in progress, project count with status completed, last top 5 activities and last top 5 projects
     */
    public DashboardTools getDashboardEmployeeTools(Long userId) {
        int totalInProgressProjects = projectRepository.findByUserIdAndStatusId(userId, 2L).size();
        int totalCompletedProjects = projectRepository.findByUserIdAndStatusId(userId, 4L).size();
        List<Activity> top5Activities = activityRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
        List<Project> top5Projects = projectRepository.findTop5ByUserIdOrderByEndDateDesc(userId);

        List<ActivityDTO> top5ActivitiesDTO = top5Activities.stream()
                .map(activity -> dtoConverter.convertToDTO(activity, dtoConverter.convertToDTO(activity.getUser())))
                .collect(Collectors.toList());

        List<ProjectDTO> top5ProjectsDTO = top5Projects.stream().map(project -> {
            UserDTO userDTO = dtoConverter.convertToDTO(project.getUser());
            return new ProjectDTO(
                    project.getId(),
                    project.getClient(),
                    userDTO,
                    project.getTitle(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getDueDate(),
                    null
            );
        }).collect(Collectors.toList());

        return new DashboardTools(
                totalInProgressProjects,
                totalCompletedProjects,
                top5ActivitiesDTO,
                top5ProjectsDTO
        );
    }
 }
