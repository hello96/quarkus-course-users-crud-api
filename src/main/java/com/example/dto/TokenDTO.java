package com.example.dto;

public class TokenDTO {
    public String token;
    public long expiresIn;

    public TokenDTO() {
    }

    public TokenDTO(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
