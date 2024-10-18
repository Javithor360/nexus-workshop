package com.nexus.client.controllers;

import com.nexus.client.models.Project;
import com.nexus.client.models.User;
import com.nexus.client.services.ProjectService;
import com.nexus.client.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/dashboard")
public class EmployeeDashboardController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ProjectService projectService;

    public EmployeeDashboardController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    // ======================================== EMPLOYEE DASHBOARD ========================================
    @GetMapping("/employee/index")
    @PreAuthorize("hasAuthority('ACCESS_EMPLOYEE_DASHBOARD')")
    public String bossDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/boss/index";
    }

    // ======================================== EMPLOYEE PROJECTS MANAGEMENT ========================================

    @GetMapping("/employee/management/project")
    @PreAuthorize("hasAuthority('ACCESS_EMPLOYEE_DASHBOARD')")
    public String projectManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Project Management");
        return "dashboard/employee/projectManagement";
    }

    @GetMapping("/employee/management/project/{id}")
    @PreAuthorize("hasAuthority('ACCESS_EMPLOYEE_DASHBOARD')")
    public String projectDetails(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Project Details");

        Project project = projectService.getProject(id, session.getAttribute("token").toString()); // Getting the specific project via id
        model.addAttribute("project", project); // Adding the attribute to the view

    //-> If the project doesn't exist it returns -> If the project doesn't belong to the current user   -> If everything is OK it returns the
    //   404 error page to avoid problems           it denies the access to the incorrect user             normal view
        return  project == null ? "error/404" : project.getUser().getId() != user.getId() ? "error/403" : "dashboard/employee/projectDetails";
    }
}