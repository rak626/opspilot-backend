package com.opspilot.backend.repository;

import com.opspilot.backend.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByPublicKey(String key);
}
