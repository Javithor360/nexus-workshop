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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class AdminDashboardController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final ProjectService projectService;

    public AdminDashboardController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @ModelAttribute
    public void commonParams(Model model){
        model.addAttribute("route", "admin");
    }

    // ======================================== ADMIN DASHBOARD ========================================

    @GetMapping("/admin/index")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "home");
        return "dashboard/index";
    }

    // ======================================== ADMIN INFO ========================================
    @GetMapping("/admin/info")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminInfo(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "dashboard/info";
    }

    // ======================================== ADMIN USERS ========================================

    @GetMapping("/admin/users")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<User> users = userService.getUsers(session.getAttribute("token").toString());
        model.addAttribute("user", user);
        model.addAttribute("activePage", "users");
        return "dashboard/users";
    }

    // ======================================== BOSS PROJECT MANAGEMENT ========================================

    @GetMapping("/admin/management/project")
    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
    public String projectManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Your Projects");
        model.addAttribute("activePage", "projects");
        return "dashboard/boss/projectManagement";
    }

    @GetMapping("/admin/management/project/{id}")
    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
    public String projectDetails(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Project Details");

        Project project = projectService.getProject(id, session.getAttribute("token").toString()); // Getting the specific project via id
        model.addAttribute("project", project); // Adding the attribute to the view

        //-> If the project doesn't exist it returns -> If everything is OK it returns the
        //   404 error page to avoid problems           normal view
        return  project == null ? "error/404" : "dashboard/boss/projectDetails";
    }

    // ======================================== ADMIN CLIENT MANAGEMENT ========================================

    @GetMapping("/admin/management/client")
    public String clientManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        model.addAttribute("pageTitle", "Client Management");
        model.addAttribute("activePage", "client");
        return "management/clientManagement";
    }

    // ======================================== BOSS EMPLOYEE MANAGEMENT ========================================

    @GetMapping("/admin/management/employee")
    public String employeeManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        model.addAttribute("pageTitle", "Employee Management");
        model.addAttribute("activePage", "employee");
        return "management/employeeManagement";
    }
}
