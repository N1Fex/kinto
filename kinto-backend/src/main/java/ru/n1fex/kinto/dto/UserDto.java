package ru.n1fex.kinto.dto;

import lombok.Data;
import ru.n1fex.kinto.entity.UserRole;
import ru.n1fex.kinto.entity.UserStatus;

@Data
public class UserDto {

    private String username;
    private String displayName;
    private UserStatus status;
    private UserRole role;
    private String bio;
    private String avatar;

}
