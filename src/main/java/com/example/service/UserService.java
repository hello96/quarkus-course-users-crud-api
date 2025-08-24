package com.example.service;

import java.util.List;

import com.example.dto.UserCreateDTO;
import com.example.dto.UserDTO;
import com.example.dto.UserUpdateDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
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

    @CacheResult(cacheName = "user-cache")
    public UserDTO get(Long id) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

    @CacheResult(cacheName = "user-cache")
    public List<UserDTO> search(@CacheKey String name, @CacheKey int page, @CacheKey int size) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        size = Math.min(Math.max(size, 1), 50);
        page = Math.max(page, 0);

        PanacheQuery<User> q = repo.findByNamePaged(name).page(Page.of(page, size));
        return q.list().stream().map(UserMapper::toDTO).toList();
    }

    @Transactional
    @CacheInvalidate(cacheName = "user-cache")
    public void delete(Long id) {
        if (!repo.deleteById(id)) {
            throw new NotFoundException();
        }
    }
}
