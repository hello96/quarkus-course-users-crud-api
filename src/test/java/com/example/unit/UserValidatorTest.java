package com.example.unit;

import com.example.dto.UserCreateDTO;
import com.example.utils.UserValidator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    @Test
    void rejectsInvalidEmail() {
        var in = new UserCreateDTO();
        in.name = "Mario";
        in.email = "bad";
        var ex = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateCreate(in));
        assertTrue(ex.getMessage().contains("email"));
    }
}