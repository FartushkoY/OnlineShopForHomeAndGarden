package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.CartItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "product.id", source = "productId")
    CartItem requestDtoToEntity(CartItemRequestDto cartItemRequestDto);

    @Mapping(target = "productId", source = "product.id")
    CartItemRequestDto entityToRequestDto (CartItem cartItem);

    Set<CartItemRequestDto> entityListToRequestDto(List<CartItem> cartItem);

    CartItem responseDtoToEntity(CartItemResponseDto cartItemDto);

    CartItemResponseDto entityToResponseDto(CartItem cartItem);

    Set<CartItemResponseDto> entityListToResponseDto(List<CartItem> cartItem);

}
