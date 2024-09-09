package de.telran.onlineshopforhomeandgarden1.dto.request;

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
public class CartRequestDto {
    private Long id;

    private String userId;

    private Set<CartItemRequestDto> items = new LinkedHashSet<>();

}
