package com.nexus.server.controllers;

import com.nexus.server.entities.beans.DashboardTools;
import com.nexus.server.services.extra.DashboardToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard-tool") // http://localhost:8080/api/dashboard-tool
public class DashboardToolController {

    private final DashboardToolService dashboardToolService;

    @Autowired
    public DashboardToolController(DashboardToolService dashboardToolService) {
        this.dashboardToolService = dashboardToolService;
    }

    /**
     * Get all the tools for the boss dashboard
     *
     * @return DashboardBossToolsDTO with project count with status in progress, project count with status completed, last top 5 activities and last top 5 projects
     * @route GET /api/dashboard-tool/boss
     */
    @GetMapping("/boss")
    public ResponseEntity<DashboardTools> getBossDashboardTools() {
        return ResponseEntity.ok(dashboardToolService.getDashboardBossTools());
    }

    /**
     * Get all the tools for the employee dashboard
     *
     * @param id Employee id
     * @return DashboardUserToolsDTO with last top 5 activities and last top 5 projects
     * @route GET /api/dashboard-tool/employee
     */
    @GetMapping("/employee/{id}")
    public ResponseEntity<DashboardTools> getEmployeeDashboardTools(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardToolService.getDashboardEmployeeTools(id));
    }
}
