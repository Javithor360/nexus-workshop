package com.nexus.repositories;

import com.nexus.entities.Project;
import com.nexus.entities.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Long> {
}