package de.telran.onlineshopforhomeandgarden1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private Long id;
    private Set<CartItemResponseDto> cartItems = new LinkedHashSet<>();
}
