package com.nexus.server.repositories;

import com.nexus.server.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserId(Long userId);

    List<Activity> findByTypeId(Long activityTypeId);

    List<Activity> findTop5AllByOrderByCreatedAtDesc();

    List<Activity> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find all activities by
     */

    List<Activity> findAllByUserId(Long userId);
}
