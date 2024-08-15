package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

   Order dtoToEntity(OrderResponseDto orderResponseDto);
   OrderResponseDto entityToDto(Order order);
   List<OrderResponseDto> entityListToDto(List<Order> orders);
}
