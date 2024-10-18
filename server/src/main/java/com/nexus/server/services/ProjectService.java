package com.nexus.server.services;

import com.nexus.server.entities.Activity;
import com.nexus.server.entities.Log;
import com.nexus.server.entities.Project;
import com.nexus.server.entities.dto.ProjectDTO;
import com.nexus.server.repositories.ILogRepository;
import com.nexus.server.repositories.IProjectRepository;
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

    @Autowired
    public ProjectService(IProjectRepository projectRepository, IProjectRepository projectRepository1, ILogRepository logRepository) {
        this.projectRepository = projectRepository;
        this.logRepository = logRepository;
    }

    /**
     * Get all projects
     * @return List of all projects
     */
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(project -> {
            List<Log> logs = logRepository.findByProjectId(project.getId()).orElseThrow();
            List<Activity> activities = logs.stream()
                    .map(Log::getActivity)
                    .collect(Collectors.toList());

            return new ProjectDTO(
                    project.getId(),
                    project.getClient(),
                    project.getUser(),
                    project.getTitle(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getDueDate(),
                    activities
            );
        }).collect(Collectors.toList());
    }

    /**
     * Get project by id
     * @param id Project id
     * @return Project
     */
    public Optional<ProjectDTO> getProjectById(Long id) {
        Optional<Project> projectOpt = projectRepository.findById(id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            List<Log> logs = logRepository.findByProjectId(project.getId()).orElseThrow();
            List<Activity> activities = logs.stream()
                    .map(Log::getActivity)
                    .collect(Collectors.toList());

            ProjectDTO projectDTO = new ProjectDTO(
                    project.getId(),
                    project.getClient(),
                    project.getUser(),
                    project.getTitle(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getDueDate(),
                    activities
            );

            return Optional.of(projectDTO);
        }
        return Optional.empty();
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
        List<ProjectDTO> projectDTOs = projects.stream().map(project -> {
            List<Log> logs = logRepository.findByProjectId(project.getId()).orElseThrow();
            List<Activity> activities = logs.stream()
                    .map(Log::getActivity)
                    .collect(Collectors.toList());

            return new ProjectDTO(
                    project.getId(),
                    project.getClient(),
                    project.getUser(),
                    project.getTitle(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getDueDate(),
                    activities
            );
        }).collect(Collectors.toList());

        return Optional.of(projectDTOs);
    }
}