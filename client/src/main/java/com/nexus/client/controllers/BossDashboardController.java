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
public class BossDashboardController {

    @Autowired
    private final UserService userService;

    public BossDashboardController(UserService userService) {
        this.userService = userService;
    }

    // ======================================== BOSS DASHBOARD ========================================
    @GetMapping("/boss/index")
    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
    public String bossDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard/boss/index";
    }

    @GetMapping("/boss/management/client")
    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
    public String clientManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Gestión de Clientes");
        return "dashboard/boss/clientManagement";
    }

    @GetMapping("/boss/management/client/get") // === Request to get clients
    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
    @ResponseBody
    public List<User> clientList(HttpSession session) {
        return userService.getUsers(session.getAttribute("token").toString());
    }

    // ======================================== Boss Projects Management ========================================

    @GetMapping("/boss/management/project")
    @PreAuthorize("hasAuthority('ACCESS_BOSS_DASHBOARD')")
    public String projectManagement(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Placeholder");
        return "dashboard/boss/projectManagement";
    }




}
