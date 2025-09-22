package com.example.repository;

import com.example.entity.User;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public Uni<User> findByEmail(String email) {
        Uni<User> result = find("email", email).firstResult();
        return result;
    }

    public PanacheQuery<User> findByNamePaged(String name) {
        if (name != null && !name.isBlank()) {
            return find("lower(name) like ?1", "%" + name.toLowerCase() + "%");
        }
        return findAll();
    }
}
