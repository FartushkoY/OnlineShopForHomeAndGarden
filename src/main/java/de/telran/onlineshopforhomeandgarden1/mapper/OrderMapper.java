package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.OrderDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

   Order dtoToEntity(OrderDto orderDto);
   OrderDto entityToDto(Order order);
   List<OrderDto> entityListToDto(List<Order> orders);
}
