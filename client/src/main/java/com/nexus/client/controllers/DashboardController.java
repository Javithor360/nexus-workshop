package com.nexus.client.controllers;

import com.nexus.client.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/employee/index")
    @PreAuthorize("hasAuthority('ACCESS_EMPLOYEE_DASHBOARD')")
    public String employeeDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/employee/index";
    }

    @GetMapping("/boss/index")
    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
    public String bossDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/boss/index";
    }

    @GetMapping("/admin/index")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/admin/index";
    }

    @GetMapping("/unauthorized")
    public String unauthorizedAccess() {
        return "error/unauthorized";
    }
}