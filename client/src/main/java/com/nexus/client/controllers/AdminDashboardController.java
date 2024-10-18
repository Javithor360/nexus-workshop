package com.nexus.client.controllers;

import com.nexus.client.models.User;
import com.nexus.client.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class AdminDashboardController {

    @Autowired
    private final UserService userService;

    public AdminDashboardController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void commonParams(Model model){
        model.addAttribute("route", "admin");
    }

    @GetMapping("/admin/index")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("activePage", "home");
        return "dashboard/index";
    }

    @GetMapping("/admin/info")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminInfo(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "dashboard/info";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasAuthority('ACCESS_ADMIN_DASHBOARD')")
    public String adminUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<User> users = userService.getUsers(session.getAttribute("token").toString());
        model.addAttribute("user", user);
        model.addAttribute("activePage", "users");
        return "dashboard/users";
    }

    @GetMapping("/admin/management/client")
    public String clientManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        model.addAttribute("pageTitle", "Client Management");
        model.addAttribute("activePage", "client");
        return "management/clientManagement";
    }

    @GetMapping("/admin/management/employee")
    public String employeeManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        model.addAttribute("pageTitle", "Employee Management");
        model.addAttribute("activePage", "employee");
        return "management/employeeManagement";
    }
}
