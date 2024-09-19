package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "product.id", source = "productId")
    CartItem requestDtoToEntity(CartItemRequestDto cartItemRequestDto);

    @Mapping(target = "productId", source = "product.id")
    CartItemRequestDto entityToRequestDto (CartItem cartItem);

    CartItemResponseDto entityToResponseDto(CartItem cartItem);

    CartItemResponseDto requestDtoToResponseDto(CartItemRequestDto cartItemRequestDto);
}
