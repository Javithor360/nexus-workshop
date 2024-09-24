package com.nexus.repositories;

import com.nexus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    // Testig the findByUsername method
    Optional<User> findByUsername(String username);

}
