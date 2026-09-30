package com.lab.infrastructure.adapter.in.function.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lab.domain.model.ProcessingRequest;

public record InitProcessingRequestDto(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("filepath") String filePath) {

    public ProcessingRequest toDomain() {
        return new ProcessingRequest(id, type, filePath);
    }
}
