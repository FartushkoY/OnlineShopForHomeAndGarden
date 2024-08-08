package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.CartDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart dtoToEntity(CartDto cartDto);
    CartDto entityToDto(Cart cart);
}
