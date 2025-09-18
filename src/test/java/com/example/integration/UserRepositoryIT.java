package com.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.entity.User;
import com.example.repository.UserRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class UserRepositoryIT {
    @Inject
    UserRepository repo;

    @Test
    @Transactional
    void persist_and_findByIdOptional() {
        var u = new User();
        u.email = "a@a.it";
        u.name = "mario";

        repo.persist(u);
        assertNotNull(u.id);

        Optional<User> found = repo.findByIdOptional(u.id);
        assertTrue(found.isPresent());
        assertEquals("Mario", found.get().name);
        assertEquals("c@ex.com", found.get().email);
    }

    @Test
    @Transactional
    void uniqueEmail_constraint_violation() {
        var u1 = new User();
        u1.email = "a@a.it";
        u1.name = "mario";

        var u2 = new User();
        u2.email = "a@a.it";
        u2.name = "wario";

        repo.persist(u2);
        assertNotNull(u1.id);

        // La seconda persist dovrebbe fallire lato DB (unique index/email)
        assertThrows(Exception.class, () -> repo.persist(u2));
    }
}
