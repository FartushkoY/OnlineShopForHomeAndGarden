package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import de.telran.onlineshopforhomeandgarden1.security.JwtAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static de.telran.onlineshopforhomeandgarden1.enums.Periods.*;
import static org.junit.jupiter.api.Assertions.*;


class OrderServiceTest {

    private static OrderService orderService;
    private static OrderRepository repository;
    private static ProductRepository productRepository;
    private static UserService userService;
    private static AuthService authService;
    private static OrderMapper orderMapper;


    @BeforeEach
    public void init() {
        repository = Mockito.mock(OrderRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        orderMapper = Mappers.getMapper(OrderMapper.class);
        userService = Mockito.mock(UserService.class);
        authService = Mockito.mock(AuthService.class);
        JwtAuthentication mockAuthInfo = Mockito.mock(JwtAuthentication.class);
        Mockito.when(authService.getAuthInfo()).thenReturn(mockAuthInfo);
        orderService = new OrderService(repository, orderMapper, productRepository, authService, userService);

        Mockito.when(mockAuthInfo.getLogin()).thenReturn("admin@gmail.com");

        User user = new User();
        user.setId(1l);
        user.setEmail("admin@gmail.com");

        Mockito.when(userService.getUserByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
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
        newOrder.setStatus(null);
        newOrder.setContactPhone(null);
        newOrder.setOrderItems(null);


        Mockito.when(repository.save(newOrder)).thenReturn(newOrder);
        orderService.addOrder(orderMapper.entityToDtoRequest(newOrder));
        Mockito.verify(repository).save(Mockito.eq(newOrder));

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

        Set<Order> deliveredOrders = Set.of(firstOrder, secondOrder);
        OrderResponseDto dtoOne = new OrderResponseDto();
        dtoOne.setId(2L);
        OrderResponseDto dtoTwo = new OrderResponseDto();
        dtoTwo.setId(3L);
        Mockito.when(repository.findOrdersByUserId(1l)).thenReturn(deliveredOrders);
        Set<OrderResponseDto> resultOrders = orderService.getOrdersHistory();

        Mockito.verify(repository).findOrdersByUserId(1l);

        assertNotNull(resultOrders);
        assertEquals(2, resultOrders.size());


    }

    @Test
    public void deleteOrder() {
        Long id = 55L;
        Order order = new Order();
        order.setId(id);
        order.setStatus(Status.PENDING);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.deleteOrder(id);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        Mockito.verify(repository).delete(Mockito.eq(order));

    }

    @Test
    public void deleteOrderEmpty() {
        Long id = 66L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Order> optional = orderService.deleteOrder(id);
        assertTrue(optional.isEmpty());

    }

    @Test
    void getRevenueReportYearTest() {
        LocalDate localDateStart = LocalDate.of(2023, 1, 1);
        LocalDateTime localDateTimeStart = LocalDateTime.of(localDateStart, LocalTime.MIN);
        Instant start = (localDateTimeStart.toInstant(ZoneOffset.UTC));

        LocalDate localDateEnd = localDateStart.plusYears(1);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateEnd, LocalTime.MIN);
        Instant endYear = (localDateTimeEnd.toInstant(ZoneOffset.UTC));

        Mockito.when(repository.getRevenueReportDetailedByYear(start, endYear)).thenReturn(new ArrayList<>());
        Mockito.when(repository.getTotalResult(start, endYear)).thenReturn(BigDecimal.valueOf(23));
        orderService.getRevenueReport(localDateStart, YEAR, 1, YEAR);
        Mockito.verify(repository).getTotalResult(start, endYear);
        Mockito.verify(repository).getRevenueReportDetailedByYear(start, endYear);
    }

    @Test
    void getRevenueReportMonthTest() {
        LocalDate localDateStart = LocalDate.of(2023, 1, 1);
        LocalDateTime localDateTimeStart = LocalDateTime.of(localDateStart, LocalTime.MIN);
        Instant start = (localDateTimeStart.toInstant(ZoneOffset.UTC));

        LocalDate localDateEnd = localDateStart.plusMonths(2);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateEnd, LocalTime.MIN);
        Instant endMonth = (localDateTimeEnd.toInstant(ZoneOffset.UTC));

        Mockito.when(repository.getRevenueReportDetailedByMonth(start, endMonth)).thenReturn(new ArrayList<>());
        Mockito.when(repository.getTotalResult(start, endMonth)).thenReturn(BigDecimal.valueOf(23));
        orderService.getRevenueReport(localDateStart, MONTH, 2, MONTH);
        Mockito.verify(repository).getTotalResult(start, endMonth);
        Mockito.verify(repository).getRevenueReportDetailedByMonth(start, endMonth);
    }


    @Test
    void getRevenueReportWeekTest() {
        LocalDate localDateStart = LocalDate.of(2023, 1, 1);
        LocalDateTime localDateTimeStart = LocalDateTime.of(localDateStart, LocalTime.MIN);
        Instant start = (localDateTimeStart.toInstant(ZoneOffset.UTC));

        LocalDate localDateEnd = localDateStart.plusMonths(2);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateEnd, LocalTime.MIN);
        Instant endMonth = (localDateTimeEnd.toInstant(ZoneOffset.UTC));

        Mockito.when(repository.getRevenueReportDetailedByWeek(start, endMonth)).thenReturn(new ArrayList<>());
        Mockito.when(repository.getTotalResult(start, endMonth)).thenReturn(BigDecimal.valueOf(23));
        orderService.getRevenueReport(localDateStart, MONTH, 2, WEEK);
        Mockito.verify(repository).getTotalResult(start, endMonth);
        Mockito.verify(repository).getRevenueReportDetailedByWeek(start, endMonth);
    }


    @Test
    void getRevenueReportDayTest() {
        LocalDate localDateStart = LocalDate.of(2023, 1, 1);
        LocalDateTime localDateTimeStart = LocalDateTime.of(localDateStart, LocalTime.MIN);
        Instant start = (localDateTimeStart.toInstant(ZoneOffset.UTC));

        LocalDate localDateEnd = localDateStart.plusDays(10);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateEnd, LocalTime.MIN);
        Instant endDay = (localDateTimeEnd.toInstant(ZoneOffset.UTC));

        Mockito.when(repository.getRevenueReportDetailedByDayOrHour(start, endDay, "%Y %m %d")).thenReturn(new ArrayList<>());
        Mockito.when(repository.getTotalResult(start, endDay)).thenReturn(BigDecimal.valueOf(23));
        orderService.getRevenueReport(localDateStart, DAY, 10, DAY);
        Mockito.verify(repository).getTotalResult(start, endDay);
        Mockito.verify(repository).getRevenueReportDetailedByDayOrHour(start, endDay, "%Y %m %d");
    }

    @Test
    void getRevenueReportHourTest() {
        LocalDate localDateStart = LocalDate.of(2023, 1, 1);
        LocalDateTime localDateTimeStart = LocalDateTime.of(localDateStart, LocalTime.MIN);
        Instant start = (localDateTimeStart.toInstant(ZoneOffset.UTC));

        LocalDate localDateEnd = localDateStart.plusDays(10);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateEnd, LocalTime.MIN);
        Instant endDay = (localDateTimeEnd.toInstant(ZoneOffset.UTC));

        Mockito.when(repository.getRevenueReportDetailedByDayOrHour(start, endDay, "%Y %m %d %H")).thenReturn(new ArrayList<>());
        Mockito.when(repository.getTotalResult(start, endDay)).thenReturn(BigDecimal.valueOf(23));
        orderService.getRevenueReport(localDateStart, DAY, 10, HOUR);
        Mockito.verify(repository).getTotalResult(start, endDay);
        Mockito.verify(repository).getRevenueReportDetailedByDayOrHour(start, endDay, "%Y %m %d %H");
    }
}
