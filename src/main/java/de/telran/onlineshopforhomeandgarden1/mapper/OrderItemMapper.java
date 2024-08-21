package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.response.OrderItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import org.mapstruct.*;
import java.util.List;


@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponseDto entityToDto(OrderItem orderItem);
    List<OrderItemResponseDto> entityListToDto(List<OrderItem> orderItems);

    }

