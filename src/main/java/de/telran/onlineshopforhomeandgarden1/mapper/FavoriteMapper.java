package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.response.FavoriteResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    Favorite dtoToEntity(FavoriteResponseDto favoriteResponseDto);

    FavoriteResponseDto entityToDto(Favorite favorite);

    Set<FavoriteResponseDto> entityListToDto(Set<Favorite> favorites);

}
