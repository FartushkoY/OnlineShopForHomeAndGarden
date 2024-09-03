package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart dtoToRequestEntity (CartRequestDto cartRequestDto);
    CartRequestDto entityToRequestDto (Cart cart);
    Set<CartRequestDto> entityListToRequestDto(List<Cart> cart);
    Cart dtoToResponseEntity(CartResponseDto cartDto);
    CartResponseDto entityToResponseDto(Cart cart);
    Set<CartResponseDto> entityListToResponseDto(Set<Cart> cart);
}
