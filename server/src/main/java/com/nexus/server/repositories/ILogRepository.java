package com.nexus.server.repositories;

import com.nexus.server.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogRepository extends JpaRepository<Log, Long> {
}
