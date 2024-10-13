package com.nexus.repositories;

import com.nexus.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserId(Long userId);

    List<Activity> findByProjectId(Long projectId);

    List<Activity> findByTypeId(Long activityTypeId);

    /**
     * Find all activities by
     */

    List<Activity> findAllByUserId(Long userId);

    List<Activity> findAllByProjectId(Long projectId);
}
