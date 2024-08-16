package de.telran.onlineshopforhomeandgarden1.mapper;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import org.mapstruct.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;


@Mapper(componentModel = "spring")
public interface OrderMapper {

   Order dtoToEntity(OrderResponseDto orderResponseDto);

   @Mapping(target = "orderItems", source = "items")
   Order dtoRequestToEntity(OrderRequestDto orderRequestDto);
   OrderResponseDto entityToDto(Order order);
   OrderRequestDto entityToDtoRequest(Order order);
   List<OrderResponseDto> entityListToDto(List<Order> orders);
   List<OrderRequestDto> entityListRequestToDto(List<Order> orders);


   @AfterMapping
   default void setOrder(@MappingTarget Order order) {
      Optional.ofNullable(order.getOrderItems())
              .ifPresent(it -> it.forEach(item -> item.setOrder(order)));

      User user = new User();
      user.setId(1L);
      order.setUser(user);
   }

   @Mapping(target = "product", source = "productId")
   OrderItem dtoToEntity(OrderItemRequestDto orderItemRequestDto);

   default Product mapProduct(String productId) {
      if (productId == null) {
         return null;
      }
      Product product = new Product();
      Long id = Long.parseLong(productId);
      product.setId(id);
      return product;
   }


   }

