package de.telran.onlineshopforhomeandgarden1.dto.response;

import de.telran.onlineshopforhomeandgarden1.dto.ProductDto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private Long id;

    @NotNull(message = "{validation.orderItem.quantity.notNull}")
    private Integer quantity;


    @DecimalMin(value = "0.0", inclusive = true, message = "{validation.orderItem.priceAtPurchase.notNull}")
    @Digits(integer = 5, fraction = 2, message = "{validation.orderItem.priceAtPurchase}")
    private BigDecimal priceAtPurchase;

    private ProductResponseDto product;

}
