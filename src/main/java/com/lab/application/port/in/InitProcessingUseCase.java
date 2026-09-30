package com.lab.application.port.in;

import com.lab.domain.model.ProcessingRequest;
import com.lab.domain.model.ProcessingResult;

/**
 * Input port: starts the processing of a file.
 */
public interface InitProcessingUseCase {

    ProcessingResult initProcessing(ProcessingRequest request);
}
