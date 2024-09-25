package com.nexus.repositories;

import com.nexus.entities.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivityTypeRepository extends JpaRepository<ActivityType, Long> {
}
