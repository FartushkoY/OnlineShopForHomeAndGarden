package de.telran.onlineshopforhomeandgarden1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class OrderItemDto {

    private Long id;

    @NotNull(message = "{validation.orderItem.quantity.notNull}")
    private Integer quantity;


    @DecimalMin(value = "0.0", inclusive = true, message = "{validation.orderItem.priceAtPurchase.notNull}")
    @Digits(integer = 5, fraction = 2, message = "{validation.orderItem.priceAtPurchase}")
    private BigDecimal priceAtPurchase;

    @JsonIgnore
    private OrderDto order;

    @JsonIgnore
    private ProductDto product;

}
