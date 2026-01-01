package com.opspilot.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "incidents",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"project_id", "hash"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 128)
    private String hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private IncidentStatus status = IncidentStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private SeverityLevel severity;

    @Column(name = "ai_summary")
    private String aiSummary;

    @Column(nullable = false)
    @Builder.Default
    private int count = 1;

    @CreationTimestamp
    @Column(name = "first_seen", nullable = false, updatable = false)
    private Instant firstSeen;

    @UpdateTimestamp
    @Column(name = "last_seen", nullable = false)
    private Instant lastSeen;

    public void incrementCount() {
        this.count++;
    }
}
