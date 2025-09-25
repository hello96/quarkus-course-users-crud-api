package com.example.resource;

import java.time.Instant;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.example.dto.LoginDTO;
import com.example.dto.TokenDTO;

import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/auth")
public class AuthResource {
    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan")
    long lifespan;

    @POST
    @Path("/token")
    public TokenDTO issue(LoginDTO login) {
        // TODO: qui validare credenziali
        String jwt = Jwt.claims()
                .subject("user-123")
                .groups(Set.of("user", "admin"))
                .issuedAt(Instant.now())
                .sign(); // Firma con la chiave privata

        return new TokenDTO(jwt, lifespan);
    }
}
