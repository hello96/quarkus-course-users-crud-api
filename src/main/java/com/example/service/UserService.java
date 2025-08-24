package com.example.service;

import java.util.List;

import com.example.dto.UserCreateDTO;
import com.example.dto.UserDTO;
import com.example.dto.UserUpdateDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository repo;

    public List<UserDTO> list() {
        return UserMapper.toDTOs(repo.listAll());
    }

    public UserDTO get(Long id) {
        return UserMapper.toDTO(
                repo.findByIdOptional(id).orElseThrow(NotFoundException::new));
    }

    @Transactional
    public UserDTO create(UserCreateDTO in) {
        // Email univoca
        repo.findByEmail(in.email).ifPresent(u -> {
            throw new WebApplicationException("Email already in use", Response.Status.CONFLICT);
        });

        User entity = UserMapper.fromCreate(in);
        repo.persist(entity);
        return UserMapper.toDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO in) {
        User existing = repo.findByIdOptional(id)
                .orElseThrow(NotFoundException::new);

        if (in.email != null) {
            repo.findByEmail(in.email).ifPresent(u -> {
                if (!u.id.equals(id)) {
                    throw new WebApplicationException("Email already in use", Response.Status.CONFLICT);
                }
            });
        }

        UserMapper.merge(existing, in);
        return UserMapper.toDTO(existing);
    }

    public List<UserDTO> search(String name, int page, int size) {
        size = Math.min(Math.max(size, 1), 50);
        page = Math.max(page, 0);

        PanacheQuery<User> q = repo.findByNamePaged(name).page(Page.of(page, size));
        return q.list().stream().map(UserMapper::toDTO).toList();
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.deleteById(id)) {
            throw new NotFoundException();
        }
    }
}
