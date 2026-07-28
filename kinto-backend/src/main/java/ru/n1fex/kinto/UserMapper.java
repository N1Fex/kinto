package ru.n1fex.kinto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.n1fex.kinto.dto.UserDto;
import ru.n1fex.kinto.dto.UserProfileDto;
import ru.n1fex.kinto.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toUserDto(User user);
    UserProfileDto toUserProfileDto(User user);
}

