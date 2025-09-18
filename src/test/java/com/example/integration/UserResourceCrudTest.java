package com.example.integration;

import com.example.resource.UserResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
class UserResourceCrudTest {

    @Test
    void create_read_update_delete_flow() {
        // CREATE
        String location = given().contentType("application/json")
                .body("""
                        {"name":"Claudio","email":"c@ex.com","active":true}
                        """)
                .when().post()
                .then()
                .statusCode(201)
                .header("Location", not(isEmptyString()))
                .extract().header("Location");

        // READ (by Location)
        given().when().get(location)
                .then().statusCode(200)
                .body("name", is("Claudio"))
                .body("email", is("c@ex.com"))
                .body("active", is(true));

        // UPDATE (PUT)
        given().contentType("application/json")
                .body("""
                        {"name":"Claudio","email":"claudio@ex.com","active":false}
                        """)
                .when().put(location.substring(location.lastIndexOf('/')))
                .then().statusCode(200)
                .body("email", is("claudio@ex.com"))
                .body("active", is(false));

        // LIST (con filtri & pagination se supportati)
        given().when().get("?active=false&page=0&size=10&sort=name,asc")
                .then().statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        // DELETE
        given().when().delete(location.substring(location.lastIndexOf('/')))
                .then().statusCode(204);

        // READ after delete => 404
        given().when().get(location)
                .then().statusCode(404);
    }
}
