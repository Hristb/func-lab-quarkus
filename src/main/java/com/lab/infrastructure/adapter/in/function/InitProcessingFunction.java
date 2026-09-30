package com.lab.infrastructure.adapter.in.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.application.port.in.InitProcessingUseCase;
import com.lab.domain.exception.InvalidProcessingRequestException;
import com.lab.domain.model.ProcessingResult;
import com.lab.infrastructure.adapter.in.function.dto.ErrorResponseDto;
import com.lab.infrastructure.adapter.in.function.dto.InitProcessingRequestDto;
import com.lab.infrastructure.adapter.in.function.dto.InitProcessingResponseDto;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import jakarta.inject.Inject;

import java.util.Optional;

/**
 * Driving adapter: exposes the InitProcessing use case as an HTTP-triggered Azure Function.
 * <p>
 * POST {host}/api/initProcessing
 * Body: {"id":"...","type":"...","filepath":"..."}
 */
public class InitProcessingFunction {

    @Inject
    InitProcessingUseCase initProcessingUseCase;

    @Inject
    ObjectMapper objectMapper;

    @FunctionName("initProcessing")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("initProcessing triggered");

        String body = request.getBody().orElse("");
        if (body.isBlank()) {
            return json(request, HttpStatus.BAD_REQUEST, new ErrorResponseDto("Request body is required"));
        }

        try {
            InitProcessingRequestDto dto = objectMapper.readValue(body, InitProcessingRequestDto.class);
            ProcessingResult result = initProcessingUseCase.initProcessing(dto.toDomain());
            context.getLogger().info("Processing accepted for id=" + result.id());
            return json(request, HttpStatus.ACCEPTED, InitProcessingResponseDto.fromDomain(result));
        } catch (JsonProcessingException e) {
            return json(request, HttpStatus.BAD_REQUEST, new ErrorResponseDto("Invalid JSON body"));
        } catch (InvalidProcessingRequestException e) {
            return json(request, HttpStatus.BAD_REQUEST, new ErrorResponseDto(e.getMessage()));
        }
    }

    private HttpResponseMessage json(HttpRequestMessage<?> request, HttpStatus status, Object payload) {
        try {
            return request.createResponseBuilder(status)
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString(payload))
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize response", e);
        }
    }
}
