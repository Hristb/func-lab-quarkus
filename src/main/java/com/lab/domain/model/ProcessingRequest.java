package com.lab.domain.model;

import com.lab.domain.exception.InvalidProcessingRequestException;

/**
 * Domain model: request to start processing a file.
 */
public record ProcessingRequest(String id, String type, String filePath) {

    public ProcessingRequest {
        requireNotBlank(id, "id");
        requireNotBlank(type, "type");
        requireNotBlank(filePath, "filepath");
    }

    private static void requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidProcessingRequestException("Field '" + field + "' is required");
        }
    }
}
