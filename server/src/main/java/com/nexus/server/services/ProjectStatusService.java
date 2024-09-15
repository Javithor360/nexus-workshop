package com.nexus.server.services;

import com.nexus.server.entities.ProjectStatus;
import com.nexus.server.repositories.IProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectStatusService {

    private final IProjectStatusRepository projectStatusRepository;

    @Autowired
    public ProjectStatusService(IProjectStatusRepository projectStatusRepository) {
        this.projectStatusRepository = projectStatusRepository;
    }

    /**
     * Get all project statuses
     * @return List of all project statuses
     */
    public List<ProjectStatus> getAllProjectStatuses() {
        return projectStatusRepository.findAll();
    }

    /**
     * Get project status by id
     * @param id Project status id
     * @return Project status
     */
    public Optional<ProjectStatus> getProjectStatusById(Long id) {
        return projectStatusRepository.findById(id);
    }

    /**
     * Create project status
     * @param projectStatus Project status
     * @return Project status
     */
    public ProjectStatus createProjectStatus(ProjectStatus projectStatus) {
        return projectStatusRepository.save(projectStatus);
    }

    /**
     * Update project status
     * @param id Project status id
     * @param projectStatusDetails Project status details
     * @return Project status
     */
    public Optional<ProjectStatus> updateProjectStatus(Long id, ProjectStatus projectStatusDetails) {
        return projectStatusRepository.findById(id)
                .map(projectStatus -> {
                    projectStatus.setName(projectStatusDetails.getName());
                    return projectStatusRepository.save(projectStatus);
                });
    }

    /**
     * Delete project status
     * @param id Project status id
     */
    public void deleteProjectStatus(Long id) {
        projectStatusRepository.deleteById(id);
    }
}
