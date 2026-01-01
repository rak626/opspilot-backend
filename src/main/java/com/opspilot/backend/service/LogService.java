package com.opspilot.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opspilot.backend.model.dto.ErrorEventDto;
import com.opspilot.backend.model.entity.Incident;
import com.opspilot.backend.model.entity.IncidentStatus;
import com.opspilot.backend.model.entity.LogEvent;
import com.opspilot.backend.model.entity.Project;
import com.opspilot.backend.repository.IncidentRepository;
import com.opspilot.backend.repository.LogEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private final LogEventRepository logRepo;
    private final IncidentRepository incidentRepo;
    private final ObjectMapper objectMapper;

    @Transactional
    public void process(ErrorEventDto dto, Project project) {

        // 1️⃣ Serialize raw payload
        String json;
        try {
            json = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            // IMPORTANT: backend must NEVER crash on bad payload
            log.error("Serialization failed for service: {}, sdk: {}", dto.appName(), dto.sdkName());
            return;
        }

        // 2️⃣ Build grouping hash
        String hash = buildIncidentHash(dto, project);

        // 3️⃣ Find existing incident OR create new
        Incident incident = incidentRepo
                .findByProjectAndHash(project, hash)
                .map(existing -> {
                    existing.incrementCount();
                    return existing;
                })
                .orElseGet(() -> Incident.builder()
                        .project(project)
                        .title(dto.message()) // use exception msg
                        .hash(hash)
                        .status(IncidentStatus.OPEN)
                        .firstSeen(Instant.now())
                        .lastSeen(Instant.now())
                        .count(1)
                        .status(IncidentStatus.OPEN)
                        .build()
                );

        incident.setLastSeen(Instant.now());

        incidentRepo.save(incident);

        // 4️⃣ Save raw log record
        saveLog(incident, json);
    }


    private String buildIncidentHash(ErrorEventDto dto, Project project) {

        // Strategy:
        // appName + error message + first stack line

        String key = project.getId() + "|" +
                dto.message() + "|" +
                firstStackLine(dto.stackTrace());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : encoded) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return String.valueOf(key.hashCode()); // fallback
        }
    }

    private String firstStackLine(String stackTrace) {
        if (stackTrace == null) return "";
        String[] lines = stackTrace.split("\n");
        return lines.length > 0 ? lines[0] : "";
    }


    public void saveLog(Incident incident, String payloadJson) {
        logRepo.save(
                LogEvent.builder()
                        .incident(incident)
                        .payloadJson(payloadJson)
                        .build()
        );
    }
}
