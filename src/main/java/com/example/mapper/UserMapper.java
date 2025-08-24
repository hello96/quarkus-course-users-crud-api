package com.example.mapper;

import java.util.List;

import com.example.dto.UserCreateDTO;
import com.example.dto.UserDTO;
import com.example.dto.UserUpdateDTO;
import com.example.entity.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserDTO toDTO(User u) {
        if (u == null)
            return null;

        UserDTO dto = new UserDTO();

        dto.id = u.id;
        dto.name = u.name;
        dto.email = u.email;
        dto.active = u.active;

        return dto;
    }

    public static List<UserDTO> toDTOs(List<User> users) {
        return users.stream().map(UserMapper::toDTO).toList();
    }

    // DTO di creazione -> nuova Entity (id lasciato a null: lo gestisce JPA)
    public static User fromCreate(UserCreateDTO in) {
        if (in == null)
            return null;
        User u = new User();
        u.name = in.name;
        u.email = in.email;
        u.active = true; // default esplicito
        return u;
    }

    // Merge parziale per update (PUT/PATCH “morbido”)
    public static void merge(User target, UserUpdateDTO in) {
        if (in == null || target == null)
            return;
        if (in.name != null && !in.name.isBlank())
            target.name = in.name;
        if (in.email != null && !in.email.isBlank())
            target.email = in.email;
        if (in.active != null)
            target.active = in.active;
    }
}
