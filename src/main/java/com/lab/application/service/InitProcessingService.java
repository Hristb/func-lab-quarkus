package com.lab.application.service;

import com.lab.application.port.in.InitProcessingUseCase;
import com.lab.domain.model.ProcessingRequest;
import com.lab.domain.model.ProcessingResult;
import com.lab.domain.model.ProcessingStatus;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InitProcessingService implements InitProcessingUseCase {

    @Override
    public ProcessingResult initProcessing(ProcessingRequest request) {
        // TODO: business logic / output ports (storage, queue, repository...)
        return new ProcessingResult(request.id(), request.type(), request.filePath(), ProcessingStatus.ACCEPTED);
    }
}
