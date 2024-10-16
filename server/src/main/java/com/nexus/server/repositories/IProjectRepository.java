package com.nexus.server.repositories;

import com.nexus.server.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);

    List<Project> findByTitle(String title);

    List<Project> findByStatusId(Long statusId);

    List<Project> findByClientId(Long clientId);

    /**
     * Find all projects by
     */

    List<Project> findAllByUserId(Long userId);
}