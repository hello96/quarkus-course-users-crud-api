package com.example.integrationmocking;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.example.dto.UserDTO;
import com.example.service.UserService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class UserResourceMockTest {

    @InjectMock
    UserService userService;

    @Test
    void getUser_ok() {
        // arrange
        UserDTO expectedDto = new UserDTO();
        expectedDto.name = "mario";

        when(userService.get(1L)).thenReturn(expectedDto);

        // act + assert
        given()
                .when().get("/user/1")
                .then()
                .statusCode(200)
                .body("name", is("mario"));
    }

    @Test
    void getUser_notFound_mapsTo404() {
        // arrange
        when(userService.get(42L)).thenThrow(new NotFoundException());

        // act + assert
        given()
                .when().get("/user/42")
                .then()
                .statusCode(404);
    }
}
