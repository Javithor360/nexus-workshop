package com.nexus.server.repositories;

import com.nexus.server.entities.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivityTypeRepository extends JpaRepository<ActivityType, Long> {
}
