package com.opspilot.backend.controller;

import com.opspilot.backend.model.dto.ErrorEventDto;
import com.opspilot.backend.model.entity.Project;
import com.opspilot.backend.repository.ProjectRepository;
import com.opspilot.backend.service.LogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Slf4j
public class LogController {

    private final LogService logService;
    private final ProjectRepository projectRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void receive(@Valid @RequestBody ErrorEventDto dto,
                        @RequestAttribute(value = "project") Project project) {

        log.info("Logs arrived from {}", dto.appName());
        logService.process(dto, project);
    }
}
