package com.nexus.repositories;

import com.nexus.entities.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
}
