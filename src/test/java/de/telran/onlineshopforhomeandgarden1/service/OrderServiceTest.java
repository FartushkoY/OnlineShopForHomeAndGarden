package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
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

import java.util.List;
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
        orderService.getOrdersHistory();
        Mockito.verify(repository).findOrdersByUserId(1l);
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
//        assertEquals(orderId, result.getId());
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


    @Test
    public void addOrder() {
        Order newOrder = new Order();
        newOrder.setId(22L);
        newOrder.setDeliveryMethod(DeliveryMethod.EXPRESS);
        newOrder.setDeliveryAddress("test address");

        Mockito.when(repository.save(newOrder)).thenReturn(newOrder);
        OrderRequestDto resultOrder = orderService.addOrder(orderMapper.entityToDtoRequest(newOrder));
        Mockito.verify(repository).save(Mockito.eq(newOrder));
        assertEquals(newOrder.getDeliveryMethod(), DeliveryMethod.valueOf(resultOrder.getDeliveryMethod()));
        assertEquals(newOrder.getDeliveryAddress(), resultOrder.getDeliveryAddress());
    }
    @Test
    public void getOrdersHistory() {
        Order firstOrder = new Order();
        Long orderIdOne = 2L;
        firstOrder.setId(orderIdOne);
        firstOrder.setStatus(Status.valueOf("DELIVERED"));

        Order secondOrder = new Order();
        Long orderIdTwo = 3L;
        secondOrder.setId(orderIdTwo);
        secondOrder.setStatus(Status.valueOf("DELIVERED"));

        List<Order> deliveredOrders = List.of(firstOrder, secondOrder);
        OrderResponseDto dtoOne = new OrderResponseDto();
        dtoOne.setId(2L);
        OrderResponseDto dtoTwo = new OrderResponseDto();
        dtoTwo.setId(3L);
        Mockito.when(repository.findOrdersByUserId(1l)).thenReturn(deliveredOrders);
        List<OrderResponseDto> resultOrders = orderService.getOrdersHistory();

        Mockito.verify(repository).findOrdersByUserId(1l);

        assertNotNull(resultOrders);
        assertEquals(2, resultOrders.size());


    }
}