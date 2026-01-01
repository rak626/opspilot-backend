package com.opspilot.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

public record ErrorEventDto(
        @NotBlank(message = "AppName/ServiceName is required")
        String appName,

        String environment,

        @NotBlank(message = "Message can't be blank")
        String message,

        @NotBlank(message = "stack-trace can't be blank")
        String stackTrace,

        @NotNull(message = "timeStamp can't be null")
        Instant timestamp,

        Map<String, Object> metadata,

        String sdkVersion,

        String sdkName) {
}
