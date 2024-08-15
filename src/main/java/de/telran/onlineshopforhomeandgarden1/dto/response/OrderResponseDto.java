package de.telran.onlineshopforhomeandgarden1.dto.response;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;

    private Instant createdAt;

    private Set<OrderItemResponseDto> orderItems;

    private String deliveryAddress;

    private String contactPhone;

    private DeliveryMethod deliveryMethod;

    private Status status;

    private Instant updatedAt;


}
