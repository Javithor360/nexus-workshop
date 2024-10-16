package com.nexus.server.services;

import com.nexus.server.entities.Project;
import com.nexus.server.repositories.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final IProjectRepository projectRepository;

    @Autowired
    public ProjectService(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Get all projects
     * @return List of all projects
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Get project by id
     * @param id Project id
     * @return Project
     */
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Get project by user id
     * @param userId User id
     * @return Projects that match the user id provided
     */
    public Optional<List<Project>> getProjectByUserId(Long userId) {
        return Optional.of(projectRepository.findByUserId(userId));
    }

    /**
     * Get project by title
     * @param title Project title
     * @return Project(s) that match the title provided
     */
    public Optional<List<Project>> getProjectByTitle(String title) {
        return Optional.of(projectRepository.findByTitle(title));
    }

    /**
     * Get project by status id
     * @param statusId Status id
     * @return Projects that match the status id provided
     */
    public Optional<List<Project>> getProjectByStatusId(Long statusId) {
        return Optional.of(projectRepository.findByStatusId(statusId));
    }

    /**
     * Get project by client id
     * @param clientId Client id
     * @return Projects that match the client id provided
     */
    public Optional<List<Project>> getProjectByClientId(Long clientId) {
        return Optional.of(projectRepository.findByClientId(clientId));
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
}
