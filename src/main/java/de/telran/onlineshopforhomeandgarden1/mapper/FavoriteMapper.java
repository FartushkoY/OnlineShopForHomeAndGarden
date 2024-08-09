package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.FavoriteDto;
import de.telran.onlineshopforhomeandgarden1.entity.Favorite;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    Favorite dtoToEntity(FavoriteDto favoriteDto);

    FavoriteDto entityToDto(Favorite favorite);

    List<FavoriteDto> entityListToDto(List<Favorite> favorites);

}
