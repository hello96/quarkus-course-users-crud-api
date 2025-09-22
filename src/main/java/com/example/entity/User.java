package com.example.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL: AUTO_INCREMENT
    public Long id;

    @NotBlank
    @Column(nullable = false, length = 80)
    public String name;

    @Email
    @NotBlank
    @Column(nullable = false, length = 120)
    public String email;

    @Column(nullable = false)
    public Boolean active = true;
}
