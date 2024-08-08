package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem dtoToEntity(CartItemMapper cartItemDtoDto);
   CartItemMapper entityToDto(CartItem cartItem);
}
