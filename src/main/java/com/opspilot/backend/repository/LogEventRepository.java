package com.opspilot.backend.repository;

import com.opspilot.backend.model.entity.Incident;
import com.opspilot.backend.model.entity.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogEventRepository extends JpaRepository<LogEvent, UUID> {

    List<LogEvent> findTop20ByIncidentOrderByCreatedAtDesc(Incident incident);

    long countByIncident(Incident incident);
}
