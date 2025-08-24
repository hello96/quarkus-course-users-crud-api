package com.example.repository;

import java.util.Optional;

import com.example.entity.User;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public Optional<User> findByEmail(String email) {
        Optional<User> result = find("email", email).firstResultOptional();
        return result;
    }

    public PanacheQuery<User> findByNamePaged(String name) {
        if (name != null && !name.isBlank()) {
            return find("lower(name) like ?1", "%" + name.toLowerCase() + "%");
        }
        return findAll();
    }
}
