package com.lab;

import com.lab.infrastructure.adapter.in.function.InitProcessingFunction;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@QuarkusTest
class InitProcessingFunctionTest {

    @Inject
    InitProcessingFunction function;

    @Test
    void validBodyReturnsAccepted() {
        HttpResponseMessage ret = invoke("{\"id\":\"1\",\"type\":\"csv\",\"filepath\":\"/data/file.csv\"}");

        assertEquals(HttpStatus.ACCEPTED, ret.getStatus());
        assertTrue(ret.getBody().toString().contains("\"filepath\":\"/data/file.csv\""));
        assertTrue(ret.getBody().toString().contains("\"status\":\"ACCEPTED\""));
    }

    @Test
    void missingFieldReturnsBadRequest() {
        HttpResponseMessage ret = invoke("{\"id\":\"1\",\"type\":\"\",\"filepath\":\"/data/file.csv\"}");

        assertEquals(HttpStatus.BAD_REQUEST, ret.getStatus());
        assertTrue(ret.getBody().toString().contains("type"));
    }

    @Test
    void invalidJsonReturnsBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, invoke("not-json").getStatus());
    }

    @Test
    void emptyBodyReturnsBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, invoke(null).getStatus());
    }

    private HttpResponseMessage invoke(String body) {
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);
        doReturn(Optional.ofNullable(body)).when(req).getBody();
        doAnswer(invocation -> new HttpResponseMessageMock.HttpResponseMessageBuilderMock()
                .status((HttpStatus) invocation.getArguments()[0]))
                .when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        return function.run(req, context);
    }
}
