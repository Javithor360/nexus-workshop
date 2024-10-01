package com.nexus.server.repositories;

import com.nexus.server.entities.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
}
