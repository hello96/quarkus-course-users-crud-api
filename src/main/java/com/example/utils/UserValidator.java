package com.example.utils;

import com.example.dto.UserCreateDTO;

public final class UserValidator {
    public static void validateCreate(UserCreateDTO in) {
        if (in.name == null || in.name.isBlank())
            throw new IllegalArgumentException("name required");
        if (in.email == null || !in.email.matches(".+@.+\\..+"))
            throw new IllegalArgumentException("email invalid");
    }
}
