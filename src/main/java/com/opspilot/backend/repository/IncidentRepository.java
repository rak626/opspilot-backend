package com.opspilot.backend.repository;

import com.opspilot.backend.model.entity.Incident;
import com.opspilot.backend.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    Optional<Incident> findByProjectAndHash(Project project, String hash);
}
