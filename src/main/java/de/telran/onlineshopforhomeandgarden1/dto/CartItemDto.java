package de.telran.onlineshopforhomeandgarden1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;

    @JsonIgnore
    private Cart cart;

    @JsonIgnore
    private Product product;

    @Min(value = 0, message = "{validation.cartItem.quantity}")
    private  int quantity;
}
