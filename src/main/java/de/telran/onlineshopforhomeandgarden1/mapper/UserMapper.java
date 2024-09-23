package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.UserRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.UserResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User requestDtoToEntity(UserRequestDto UserRequestDto);
    UserRequestDto entityToRequestDto (User User);
    UserResponseDto entityToResponseDto(User User);
}
