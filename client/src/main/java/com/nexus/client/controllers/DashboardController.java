package com.nexus.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/dashboard/employee")
    public String employeeDashboard() {
        return "dashboard/employee/index";
    }

    @GetMapping("/dashboard/boss")
    public String bossDashboard() {
        return "dashboard/boss/index";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("pageTitle", "Inicio");
        return "dashboard/admin/index";
    }

    @GetMapping("/dashboard/admin/management/client")
    public String clientManagement(Model model) {
        model.addAttribute("pageTitle", "Manejo de Clientes");
        return "dashboard/admin/clientManagement";
    }
}