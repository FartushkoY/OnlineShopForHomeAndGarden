package de.telran.onlineshopforhomeandgarden1.mapper;


import de.telran.onlineshopforhomeandgarden1.dto.request.OrderItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import org.mapstruct.*;
import java.util.List;
import java.util.Optional;


@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponseDto entityToDto(OrderItem orderItem);
    List<OrderItemResponseDto> entityListToDto(List<OrderItem> orderItems);

    }

