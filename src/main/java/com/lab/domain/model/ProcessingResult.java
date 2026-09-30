package com.lab.domain.model;

/**
 * Domain model: outcome of starting a processing request.
 */
public record ProcessingResult(String id, String type, String filePath, ProcessingStatus status) {
}
