package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart requestDtoToEntity(CartRequestDto cartRequestDto);

    @Mapping(target = "items", source = "cartItems")
    CartRequestDto entityToRequestDto (Cart cart);

    CartResponseDto entityToResponseDto(Cart cart);
}
