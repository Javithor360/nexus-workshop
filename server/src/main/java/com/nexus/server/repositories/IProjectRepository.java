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

    List<Project> findByUserIdAndStatusId(Long userId, Long statusId);

    List<Project> findTop5AllByOrderByEndDateDesc();

    List<Project> findTop5ByUserIdOrderByEndDateDesc(Long userId);
}