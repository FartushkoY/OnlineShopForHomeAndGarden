package de.telran.onlineshopforhomeandgarden1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderDto {

    private Long id;

    private Instant createdAt;

    @NotNull(message = "{validation.order.deliveryAddress.notNull}")
    @Length(max = 100, message = "{validation.order.deliveryAddress.size}")
    private String deliveryAddress;

    @NotNull(message = "{validation.order.contactPhone.notNull}")
    @Length(max = 13, message = "{validation.order.contactPhone.size}")
    private String contactPhone;

    @NotNull(message = "{validation.order.deliveryMethod.notNull}")
    private DeliveryMethod deliveryMethod;

    @NotNull(message = "{validation.order.status.notNull}")
    private Status status;

    private Instant updatedAt;

//    private UserDto user;

}
