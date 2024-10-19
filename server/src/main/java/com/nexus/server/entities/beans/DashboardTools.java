package com.nexus.server.entities.beans;

import com.nexus.server.entities.dto.ActivityDTO;
import com.nexus.server.entities.dto.ProjectDTO;

import java.util.List;

public class DashboardTools {
    private int totalInProgressProjects;
    private int totalCompletedProjects;
    private List<ActivityDTO> top5Activities;
    private List<ProjectDTO> top5Projects;

    public DashboardTools(int totalInProgressProjects, int totalCompletedProjects, List<ActivityDTO> top5Activities, List<ProjectDTO> top5Projects) {
        this.totalInProgressProjects = totalInProgressProjects;
        this.totalCompletedProjects = totalCompletedProjects;
        this.top5Activities = top5Activities;
        this.top5Projects = top5Projects;
    }

    public int getTotalInProgressProjects() {
        return totalInProgressProjects;
    }

    public void setTotalInProgressProjects(int totalInProgressProjects) {
        this.totalInProgressProjects = totalInProgressProjects;
    }

    public int getTotalCompletedProjects() {
        return totalCompletedProjects;
    }

    public void setTotalCompletedProjects(int totalCompletedProjects) {
        this.totalCompletedProjects = totalCompletedProjects;
    }

    public List<ActivityDTO> getTop5Activities() {
        return top5Activities;
    }

    public void setTop5Activities(List<ActivityDTO> top5Activities) {
        this.top5Activities = top5Activities;
    }

    public List<ProjectDTO> getTop5Projects() {
        return top5Projects;
    }

    public void setTop5Projects(List<ProjectDTO> top5Projects) {
        this.top5Projects = top5Projects;
    }
}
