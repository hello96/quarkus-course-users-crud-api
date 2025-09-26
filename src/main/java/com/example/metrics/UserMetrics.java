package com.example.metrics;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.metrics.annotation.Gauge;

import com.example.service.UserService;

import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
public class UserMetrics {

    @Inject
    private UserService service;

    public UserMetrics(UserService service) {
        this.service = service;
    }

    @Gauge(name = "active_users", unit = "none", description = "Numero di utenti attivi")
    public int activeUsers() {
        return service.list().size();
    }
}