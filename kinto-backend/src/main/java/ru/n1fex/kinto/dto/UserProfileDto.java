package ru.n1fex.kinto.dto;

import lombok.Data;
import ru.n1fex.kinto.entity.UserRole;
import ru.n1fex.kinto.entity.UserStatus;

import java.time.LocalDateTime;

@Data
public class UserProfileDto {

    private Long id;
    private String username;
    private String email;
    private String displayName;
    private UserStatus status;

    private UserRole role;
    private String bio;
    private String avatar;

    private LocalDateTime createdAt;
}
