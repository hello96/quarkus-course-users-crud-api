package com.example.service;

import java.util.List;

import com.example.dto.UserCreateDTO;
import com.example.dto.UserDTO;
import com.example.dto.UserUpdateDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository repo;

    // READ-ONLY → @WithSession
    @WithSession
    public Uni<List<UserDTO>> list() {
        return repo.listAll()
                .onItem().transform(UserMapper::toDTOs);
    }

    @WithSession
    public Uni<UserDTO> get(Long id) {
        return repo.findById(id) // Uni<User> (può essere null)
                .onItem().ifNotNull().transform(UserMapper::toDTO)
                .onItem().ifNull().failWith(new NotFoundException());
    }

    // WRITE → @WithTransaction
    @WithTransaction
    public Uni<UserDTO> create(UserCreateDTO in) {
        return repo.findByEmail(in.email) // Uni<User> (null se non esiste)
                .onItem().ifNotNull()
                .failWith(() -> new WebApplicationException("Email already in use", Response.Status.CONFLICT))
                .replaceWith(UserMapper.fromCreate(in)) // User nuovo
                .flatMap(user -> repo.persistAndFlush(user) // Uni<Void>
                        .replaceWith(UserMapper.toDTO(user))); // Uni<UserDTO>
    }

    @WithTransaction
    public Uni<UserDTO> update(Long id, UserUpdateDTO in) {
        return repo.findById(id)
                .onItem().ifNull().failWith(new NotFoundException())
                // Side-job asincrono per il controllo unicità email, senza cambiare l'item
                .call(existing -> {
                    if (in.email == null || in.email.equals(existing.email)) {
                        return Uni.createFrom().voidItem();
                    }
                    return repo.find("email", in.email).firstResult() // Uni<User> (può essere null)
                            .onItem().invoke(found -> {
                                if (found != null && !found.id.equals(existing.id)) {
                                    throw new WebApplicationException(
                                            "Email already in use: " + found.email, Response.Status.CONFLICT);
                                }
                            })
                            .replaceWithVoid();
                })
                .invoke(existing -> UserMapper.merge(existing, in)) // modifica entità managed
                .map(UserMapper::toDTO); // al commit viene flushata
    }

    @WithSession
    public Uni<List<UserDTO>> search(String name, int page, int size) {
        size = Math.min(Math.max(size, 1), 50);
        page = Math.max(page, 0);

        PanacheQuery<User> q = repo.findByNamePaged(name).page(Page.of(page, size));
        return q.list()
                .onItem().transform(users -> users.stream().map(UserMapper::toDTO).toList());
    }

    @WithTransaction
    public Uni<Void> delete(Long id) {
        return repo.deleteById(id) // Uni<Boolean>
                .flatMap(success -> success
                        ? Uni.createFrom().voidItem()
                        : Uni.createFrom().failure(new NotFoundException()));
    }
}
