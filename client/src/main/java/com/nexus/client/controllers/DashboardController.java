package com.nexus.client.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        switch (role) {
            case "EMPLOYEE":
                return "redirect:/dashboard/employee";
            case "BOSS":
                return "redirect:/dashboard/boss";
            case "ADMIN":
                return "redirect:/dashboard/admin";
            default:
                return "redirect:/";
        }
    }

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