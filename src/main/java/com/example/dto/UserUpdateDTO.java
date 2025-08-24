package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @Size(min = 2, max = 80)
    public String name;

    @Email
    public String email;

    public Boolean active;
}
