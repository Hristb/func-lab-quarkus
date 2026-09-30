package com.lab;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusIntegrationTest
public class InitProcessingFunctionIT {

    @Test
    public void testIt() {
        given()
                .contentType("application/json")
                .body("{\"id\":\"1\",\"type\":\"csv\",\"filepath\":\"/data/file.csv\"}")
                .when().post("/api/initProcessing")
                .then()
                .statusCode(202)
                .body("status", is("ACCEPTED"));
    }
}
