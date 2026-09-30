package com.lab.infrastructure.adapter.in.function.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lab.domain.model.ProcessingResult;

public record InitProcessingResponseDto(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("filepath") String filePath,
        @JsonProperty("status") String status) {

    public static InitProcessingResponseDto fromDomain(ProcessingResult result) {
        return new InitProcessingResponseDto(result.id(), result.type(), result.filePath(), result.status().name());
    }
}
