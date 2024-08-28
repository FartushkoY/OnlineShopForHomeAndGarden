package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.UserDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.UserResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToEntity(UserDto userDto);
    UserDto entityToDto(User user);
    List<UserDto> entityListToDto(List<User> users);

    User dtoToRequestEntity (UserRequestDto UserRequestDto);
    UserRequestDto entityToRequestDto (User User);
    Set<UserRequestDto> entityListToRequestDto(List<User> User);
    User dtoToResponseEntity(UserResponseDto UserDto);
    UserResponseDto entityToResponseDto(User User);
    Set<UserResponseDto> entityListToResponseDto(List<User> User);
}
