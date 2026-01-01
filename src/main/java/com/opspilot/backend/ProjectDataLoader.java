package com.opspilot.backend;

import com.opspilot.backend.model.entity.Project;
import com.opspilot.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ProjectDataLoader {

    private final ProjectRepository projectRepository;

    @Bean
    public ApplicationRunner loadProjectsOnStartup() {
        return args -> {

            List<Project> defaults = List.of(
                    new Project(null, "Demo Service", "pk_test_123", null),
                    new Project(null, "Checkout Service", "pk_checkout_001", null),
                    new Project(null, "Notification Service", "pk_notify_001", null));

            for (Project p : defaults) {

                projectRepository.findByPublicKey(p.getPublicKey()).ifPresentOrElse(existing -> log.info("Project already exists: {}", existing.getName()), () -> {
                    projectRepository.save(p);
                    log.info("Created project: {} ({})", p.getName(), p.getPublicKey());
                });
            }
        };
    }
}
