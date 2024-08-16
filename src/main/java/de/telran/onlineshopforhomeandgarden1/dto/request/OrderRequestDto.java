package de.telran.onlineshopforhomeandgarden1.dto.request;


import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {


    private Long id;

    @NotNull(message = "{validation.order.deliveryAddress.notNull}")
    @Length(max = 100, message = "{validation.order.deliveryAddress.size}")
    private String deliveryAddress;


    @NotNull(message = "{validation.order.deliveryMethod.notNull}")
    private String deliveryMethod;

    private Set<OrderItemRequestDto> items;





}
