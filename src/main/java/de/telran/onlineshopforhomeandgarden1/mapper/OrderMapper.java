package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Validated
@Mapper(componentModel = "spring")
public abstract class OrderMapper {

  public abstract Order dtoToEntity(OrderResponseDto orderResponseDto);

   @Mapping(target = "orderItems", source = "items")
   public abstract Order dtoRequestToEntity(OrderRequestDto orderRequestDto);
   public abstract OrderResponseDto entityToDto(Order order);
   @Mapping(target = "items", source = "orderItems")
   public abstract OrderRequestDto entityToDtoRequest(Order order);
   public abstract Set<OrderResponseDto> entityListToDto(Set<Order> orders);




   @AfterMapping
    void setOrder(@MappingTarget Order order) {
      if(order.getOrderItems() != null) {
         order.getOrderItems().forEach(item -> item.setOrder(order));
      }

   }

   @Mapping(target = "product.id", source = "productId")
   public abstract OrderItem dtoToEntity(@Valid OrderItemRequestDto orderItemRequestDto);

    @Mapping(source = "product.id",  target= "productId")
    public abstract OrderItemRequestDto entityToDto(@Valid OrderItem orderItem);

   }

