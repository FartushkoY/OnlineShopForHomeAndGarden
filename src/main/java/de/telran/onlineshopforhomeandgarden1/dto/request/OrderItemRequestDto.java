package de.telran.onlineshopforhomeandgarden1.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {

    @NotNull(message = "{validation.orderItem.quantity.notNull}")
    @Min(value = 1, message = "{validation.orderItem.quantity.minQuantity}")
    private Integer quantity;

    private String productId;



}
