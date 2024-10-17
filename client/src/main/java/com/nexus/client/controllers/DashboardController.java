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

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    /*
        YOU MAY WANT TO SEPARATE DASHBOARD TYPES
        INTO INDIVIDUAL CONTROLLERS PER ROLE
     */

    // ======================================== EMPLOYEE DASHBOARD ========================================
    /*
    @GetMapping("/employee/index")
    @PreAuthorize("hasAuthority('ACCESS_EMPLOYEE_DASHBOARD')")
    public String employeeDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/employee/index";
    }

    // ======================================== BOSS DASHBOARD ========================================
//    @GetMapping("/boss/index")
//    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
//    public String bossDashboard(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        model.addAttribute("user", user);
//        return "dashboard/boss/index";
//    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("pageTitle", "Inicio");
        return "dashboard/admin/index";
    }

    @GetMapping("/dashboard/admin/management/project")
    public String projectManagement(Model model) {
        model.addAttribute("pageTitle", "Gestion de Proyectos");
        return "dashboard/admin/projectManagement";
    }

    */
  
    // ======================================== ADMIN DASHBOARD ========================================

    @GetMapping("/admin/index")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/admin/index";
    }

    @GetMapping("/admin/info")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminInfo(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/admin/info";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<User> users = userService.getUsers(session.getAttribute("token").toString());
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "dashboard/admin/users";
    }

    @GetMapping("/dashboard/admin/management/employee")
    public String employeeManagement(Model model) {
        model.addAttribute("pageTitle", "Empleados");
        return "dashboard/admin/employeeManagement";
    }


    // ======================================== EXTRA ========================================
    @GetMapping("/unauthorized")
    public String unauthorizedAccess() {
        return "error/403";
    }
    
}