package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import org.mapstruct.*;
import java.util.List;


@Mapper(componentModel = "spring")
public abstract class OrderMapper {

  public abstract Order dtoToEntity(OrderResponseDto orderResponseDto);

   @Mapping(target = "orderItems", source = "items")
   public abstract Order dtoRequestToEntity(OrderRequestDto orderRequestDto);
   public abstract OrderResponseDto entityToDto(Order order);
   public abstract OrderRequestDto entityToDtoRequest(Order order);
   public abstract List<OrderResponseDto> entityListToDto(List<Order> orders);
   public abstract List<OrderRequestDto> entityListRequestToDto(List<Order> orders);


   @AfterMapping
    void setOrder(@MappingTarget Order order) {
      if(order.getOrderItems() != null) {
         order.getOrderItems().forEach(item -> item.setOrder(order));
      }

   }

   @Mapping(target = "product.id", source = "productId")
   public abstract OrderItem dtoToEntity(OrderItemRequestDto orderItemRequestDto);

   }

