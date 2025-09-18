package com.example.integrationmocking;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.example.dto.UserDTO;
import com.example.service.UserService;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;

import org.junit.jupiter.api.Test;

@QuarkusTest
class UserResourceSpyTest {

    // Inietta il vero UserService, ma “spiato” da Mockito
    @InjectSpy
    UserService userService;

    @Test
    void getUser_overrideWithSpy() {
        // Stubbing di un singolo metodo (il resto rimane reale)
        UserDTO expectedDto = new UserDTO();
        expectedDto.name = "mario";

        doReturn(expectedDto)
                .when(userService).get(99L);

        given()
                .when().get("/user/99")
                .then()
                .statusCode(200)
                .body("name", is("mario"));

        // Verifica che il metodo sia stato invocato
        verify(userService).get(99L);
    }
}
