package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.response.OrderItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;

import java.util.List;

public interface OrderItemMapper {

    OrderItem dtoToEntity(OrderItemResponseDto orderItemResponseDto);

    OrderItemResponseDto entityToDto(OrderItem orderItem);

    List<OrderItemResponseDto> entityListToDto(List<OrderItem> orderItems);

}
