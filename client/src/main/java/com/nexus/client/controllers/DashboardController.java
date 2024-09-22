package com.nexus.client.controllers;

import org.springframework.stereotype.Controller;
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
    public String adminDashboard() {
        return "dashboard/admin/index";
    }
}