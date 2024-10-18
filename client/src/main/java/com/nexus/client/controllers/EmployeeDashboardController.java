package com.nexus.client.controllers;

import com.nexus.client.models.User;
import com.nexus.client.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class EmployeeDashboardController {

    @Autowired
    private final UserService userService;

    public EmployeeDashboardController(UserService userService) {
        this.userService = userService;
    }

    // ======================================== Employee Dashboard ========================================
    @GetMapping("/employee/index")
    @PreAuthorize("hasAuthority('ACCESS_EMPLOYEE_DASHBOARD')")
    public String bossDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/boss/index";
    }

    // ======================================== Employee Projects Management ========================================

    @GetMapping("/employee/management/project")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public String projectManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Placeholder");
        return "dashboard/employee/projectManagement";
    }

    @GetMapping("/employee/management/projectDetails")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public String projectDetails(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Placeholder");
        return "dashboard/employee/projectDetails";
    }
}
