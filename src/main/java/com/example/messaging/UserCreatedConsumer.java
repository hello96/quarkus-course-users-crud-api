package com.example.messaging;

import com.example.events.UserCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class UserCreatedConsumer {

    private static final Logger LOG = Logger.getLogger(UserCreatedConsumer.class);

    @Incoming("users-in")
    public void onMessage(UserCreatedEvent event) {
        LOG.infof("Consumer ricevuto evento: %s per userId=%d",
                event.eventId(),
                event.payload().id());
        // qui puoi fare logica di business, invio mail, ecc.
    }
}
