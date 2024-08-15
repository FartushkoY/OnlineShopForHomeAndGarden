package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


class OrderServiceTest {

    private static OrderService orderService;
    private static OrderRepository repository;
    private static ProductRepository productRepository;
    private static OrderMapper orderMapper;



    @BeforeEach
    public void init() {
        repository = Mockito.mock(OrderRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        orderMapper = Mappers.getMapper(OrderMapper.class);
        orderService = new OrderService(repository, orderMapper, productRepository);

    }
    @Test
    void getAll() {
        orderService.getAll();
        Mockito.verify(repository).findAll();
    }

    @Test
    void getOrderStatus() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Status.valueOf("PAID"));
        order.setDeliveryMethod(DeliveryMethod.EXPRESS);

        Mockito.when(repository.findById(orderId)).thenReturn(Optional.of(order));
       OrderResponseDto result = orderService.getOrderStatus(orderId).get();

        Mockito.verify(repository).findById(orderId);
        assertEquals(orderId, result.getId());
        assertEquals(Status.PAID, result.getStatus());
        assertEquals(DeliveryMethod.EXPRESS, result.getDeliveryMethod());

    }

    @Test
    void getOrderStatusNull() {
        Long orderId = 1L;


        Mockito.when(repository.findById(orderId)).thenReturn(Optional.empty());
        Optional<OrderResponseDto> result = orderService.getOrderStatus(orderId);

        Mockito.verify(repository).findById(orderId);
        assertTrue(result.isEmpty());

    }
}