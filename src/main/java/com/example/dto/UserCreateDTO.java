package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateDTO {
    @NotBlank
    @Size(min = 2, max = 80)
    public String name;

    @Email
    @NotBlank
    public String email;
}
