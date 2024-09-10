package de.telran.onlineshopforhomeandgarden1.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDto {
    private String productId;

    @Min(value = 1, message = "{validation.cartItem.quantity}")
    private Integer quantity;
}
