package com.example.events;

import java.time.Instant;

public record UserCreatedEvent(
        String eventType,
        String eventId,
        Instant timestamp,
        Payload payload) {
    public record Payload(Long id, String name, String email) {
    }
}
