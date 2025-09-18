package com.example.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class UserResourceIT {
    @Test
    void testCreateUser() {
        given()
                .when().post("/user")
                .then()
                .body("name", is("mario"))
                .statusCode(201);

        given()
                .when().get("/1")
                .then()
                .statusCode(200)
                .body("name", is("mario"));
    }
}
