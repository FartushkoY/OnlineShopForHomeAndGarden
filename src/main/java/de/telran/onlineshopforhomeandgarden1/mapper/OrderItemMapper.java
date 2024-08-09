package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.OrderItemDto;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;

import java.util.List;

public interface OrderItemMapper {

    OrderItem dtoToEntity(OrderItemDto orderItemDto);

    OrderItemDto entityToDto(OrderItem orderItem);

    List<OrderItemDto> entityListToDto(List<OrderItem> orderItems);

}
