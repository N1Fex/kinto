package ru.n1fex.kinto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 64)
    private String username;
    @Column(unique = true, nullable = false, length = 254)
    private String email;

    @Column(name = "display_name", length = 64)
    private String displayName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String bio;
    private String avatar;

    @Column(length = 256)
    private String password;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}