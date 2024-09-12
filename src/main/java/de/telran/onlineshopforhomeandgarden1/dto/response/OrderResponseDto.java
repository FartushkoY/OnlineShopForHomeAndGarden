package de.telran.onlineshopforhomeandgarden1.dto.response;

import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;

    private Instant createdAt;

    private List<OrderItemResponseDto> orderItems =new ArrayList<>();

    private String deliveryAddress;

    private String contactPhone;

    private DeliveryMethod deliveryMethod;

    private Status status;

    private Instant updatedAt;

    private BigDecimal total;


}
