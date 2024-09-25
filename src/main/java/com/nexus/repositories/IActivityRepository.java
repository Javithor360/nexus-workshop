package com.nexus.repositories;

import com.nexus.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivityRepository extends JpaRepository<Activity, Long> {
}
