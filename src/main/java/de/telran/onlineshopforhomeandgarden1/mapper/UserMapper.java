package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.UserResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User requestDtoToEntity(UserRequestDto UserRequestDto);
    UserRequestDto entityToRequestDto (User User);
    Set<UserRequestDto> entityListToRequestDto(List<User> User);
    User responseDtoToEntity(UserResponseDto UserDto);
    UserResponseDto entityToResponseDto(User User);
    Set<UserResponseDto> entityListToResponseDto(List<User> User);
}
