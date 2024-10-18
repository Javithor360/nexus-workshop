package com.nexus.server.services;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.Log;
import com.nexus.server.entities.Project;
import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.entities.dto.ProjectDTO;
import com.nexus.server.entities.dto.UserDTO;
import com.nexus.server.repositories.ILogRepository;
import com.nexus.server.repositories.IProjectRepository;
import com.nexus.server.services.extra.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final IProjectRepository projectRepository;
    private final ILogRepository logRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public ProjectService(IProjectRepository projectRepository, ILogRepository logRepository, DTOConverter dtoConverter) {
        this.projectRepository = projectRepository;
        this.logRepository = logRepository;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Get all projects
     * @return List of all projects
     */
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Get project by id
     * @param id Project id
     * @return Project
     */
    public Optional<ProjectDTO> getProjectById(Long id) {
        return projectRepository.findById(id).map(this::convertToDTO);
    }

    /**
     * Get project by user id
     * @param userId User id
     * @return Projects that match the user id provided
     */
    public Optional<List<ProjectDTO>> getProjectByUserId(Long userId) {
        List<Project> projects = projectRepository.findByUserId(userId);
        return appendActivities(projects);
    }

    /**
     * Get project by title
     * @param title Project title
     * @return Project(s) that match the title provided
     */
    public Optional<List<ProjectDTO>> getProjectByTitle(String title) {
        List<Project> projects = projectRepository.findByTitle(title);
        return appendActivities(projects);
    }

    /**
     * Get project by status id
     * @param statusId Status id
     * @return Projects that match the status id provided
     */
    public Optional<List<ProjectDTO>> getProjectByStatusId(Long statusId) {
        List<Project> projects = projectRepository.findByStatusId(statusId);
        return appendActivities(projects);
    }

    /**
     * Get project by client id
     * @param clientId Client id
     * @return Projects that match the client id provided
     */
    public Optional<List<ProjectDTO>> getProjectByClientId(Long clientId) {
        List<Project> projects = projectRepository.findByClientId(clientId);
        return appendActivities(projects);
    }

    /**
     * Create project
     * @param project Project
     * @return Project
     */
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    /**
     * Update project
     * @param id Project id
     * @param projectDetails Project details
     * @return Project
     */
    public Optional<Project> updateProject(Long id, Project projectDetails) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setClient(projectDetails.getClient());
                    project.setUser(projectDetails.getUser());
                    project.setTitle(projectDetails.getTitle());
                    project.setDescription(projectDetails.getDescription());
                    project.setStatus(projectDetails.getStatus());
                    project.setStartDate(projectDetails.getStartDate());
                    project.setEndDate(projectDetails.getEndDate());
                    project.setDueDate(projectDetails.getDueDate());
                    return projectRepository.save(project);
                });
    }

    /**
     * Delete project
     * @param id Project id
     */
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // Helper method to append activities to projects
    private Optional<List<ProjectDTO>> appendActivities(List<Project> projects) {
        List<ProjectDTO> projectDTOs = projects.stream().map(this::convertToDTO).collect(Collectors.toList());
        return Optional.of(projectDTOs);
    }

    // Helper method to convert Project to ProjectDTO
    private ProjectDTO convertToDTO(Project project) {
        List<Log> logs = logRepository.findByProjectId(project.getId()).orElseThrow();
        List<ActivityDTO> activityDTOs = logs.stream()
                .map(Log::getActivity)
                .map(activity -> dtoConverter.convertToDTO(activity, dtoConverter.convertToDTO(activity.getUser())))
                .collect(Collectors.toList());

        UserDTO userDTO = dtoConverter.convertToDTO(project.getUser());

        return dtoConverter.convertToDTO(project, userDTO, activityDTOs);
    }
}