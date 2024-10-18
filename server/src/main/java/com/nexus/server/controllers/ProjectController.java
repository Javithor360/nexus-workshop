package com.nexus.server.controllers;

import com.nexus.server.entities.Project;
import com.nexus.server.entities.dto.ProjectDTO;
import com.nexus.server.services.ProjectService;
import com.nexus.server.utils.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects") // http://localhost:8081/api/projects
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Get all projects
     * @return List of all projects
     * @route GET /api/projects
     */
    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    /**
     * Get project by id
     * @param id Project id
     * @return Project
     * @route GET /api/projects/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id '" + id + "' not found")));
    }

    /**
     * <strong>⚠️ USE USER CONTROLLER INSTEAD ⚠️</strong><br>
     * Get all the projects assigned to a user id
     * @param userId User id
     * @return Projects that match the user id provided
     * @route GET /api/projects/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectDTO>> getProjectByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(projectService.getProjectByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No projects assigned to user with id '" + userId + "' found")));
    }

    /**
     * Get project by title
     * @param title Project title
     * @return Project(s) that match the title provided
     * @route GET /api/projects/title/{title}
     */
    @GetMapping("/title/{title}")
    public ResponseEntity<List<ProjectDTO>> getProjectByTitle(@PathVariable String title) {
        return ResponseEntity.ok(projectService.getProjectByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("No projects with title '" + title + "' found")));
    }

    /**
     * Get project by status id
     * @param statusId Status id
     * @return Projects that match the status id provided
     * @route GET /api/projects/status/{statusId}
     */
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<ProjectDTO>> getProjectByStatusId(@PathVariable Long statusId) {
        return ResponseEntity.ok(projectService.getProjectByStatusId(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("No projects with status id '" + statusId + "' found")));
    }

    /**
     * <strong>⚠️ USE CLIENT CONTROLLER INSTEAD ⚠️</strong><br>
     * Get project by client id
     * @param clientId Client id
     * @return Projects that match the client id provided
     * @route GET /api/projects/client/{clientId}
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProjectDTO>> getProjectByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(projectService.getProjectByClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("No projects with client id '" + clientId + "' found")));
    }

    /**
     * Create project<br>
     * Request Body
     * <pre>
     *     {
     *        "client": {
     *          "id": 1
     *        },
     *        "user": {
     *          "id": 1
     *        },
     *        "title": "Project Title",
     *        "description": "Project Description",
     *        "status": {
     *          "id": 1
     *        },
     *        "startDate": "2024-10-01",
     *        "endDate": "2024-10-31",
     *        "dueDate": "2024-10-31"
     *     }
     * </pre>
     * @param project Project
     * @return Project
     * @route POST /api/projects
     */

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    /**
     * Update project
     * @param id Project id
     * @param project Project
     * @return Project
     * @route PUT /api/projects/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @Valid @RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(id, project)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id '" + id + "' not found")));
    }

    /**
     * Delete project
     * @param id Project id
     * @return Project
     * @route DELETE /api/projects/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Long id) {
        if (projectService.getProjectById(id).isEmpty()) {
            throw new ResourceNotFoundException("Project with id '" + id + "' not found");
        }

        projectService.deleteProject(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Project deleted successfully");
        return ResponseEntity.ok(response);
    }
}
