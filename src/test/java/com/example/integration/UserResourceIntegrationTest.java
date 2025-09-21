package com.example.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class UserResourceIntegrationTest {
    @Test
    void testCreateUser() {
        given().contentType("application/json").body("{\"name\": \"mario\", \"email\": \"mario@example.com\"}").when()
                .post("/user").then()
                .body("name", is("mario")).statusCode(201);

        given()
                .when().get("/user/1")
                .then()
                .statusCode(200)
                .body("name", is("mario"));
    }
}
