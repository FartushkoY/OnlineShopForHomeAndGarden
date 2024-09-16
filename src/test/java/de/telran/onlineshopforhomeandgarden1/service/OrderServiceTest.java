package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.OrderItem;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.exception.CannotDeleteOrderException;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import de.telran.onlineshopforhomeandgarden1.security.JwtAuthentication;
import jakarta.persistence.EntityNotFoundException;
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

        Mockito.when(mockAuthInfo.getLogin()).thenReturn("customer@gmail.com");

        User user = new User();
        user.setId(1l);
        user.setEmail("admin@gmail.com");
        Mockito.when(userService.getUserByEmail("customer@gmail.com")).thenReturn(Optional.of(user));
    }

    @Test
    public void getOrdersHistorySuccess() {
        User user = userService.getUserByEmail("customer@gmail.com").orElseThrow();
        Order order = new Order();
        order.setUser(user);
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(2);
        orderItem1.setPriceAtPurchase(new BigDecimal("10.00"));
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(3);
        orderItem2.setPriceAtPurchase(new BigDecimal("5.00"));

        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);
        order.setOrderItems(orderItems);
        List<Order> orders = Collections.singletonList(order);

        Mockito.when(repository.findOrdersByUser(user)).thenReturn(orders);
        List<OrderResponseDto> result = orderService.getOrdersHistory();
        Mockito.verify(repository).findOrdersByUser(user);
        assertNotNull(result);
        assertEquals(1, result.size());
        OrderResponseDto orderResponseDto = result.get(0);
        assertEquals(new BigDecimal("35.00"), orderResponseDto.getTotal());
    }

    @Test
    public void getOrderStatus() {
        Order order = new Order();
        Long orderId = 1L;
        order.setId(orderId);
        order.setStatus(Status.valueOf("PAID"));
        order.setDeliveryMethod(DeliveryMethod.EXPRESS);
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(1);
        orderItem1.setPriceAtPurchase(new BigDecimal("10.00"));
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(4);
        orderItem2.setPriceAtPurchase(new BigDecimal("5.00"));

        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));

        Mockito.when(repository.findOrderByUserAndId(Mockito.any(User.class), Mockito.eq(orderId))).thenReturn(Optional.of(order));
        OrderResponseDto result = orderService.getOrderStatus(orderId).get();

        Mockito.verify(repository).findOrderByUserAndId(Mockito.any(User.class), Mockito.eq(orderId));
        assertEquals(Status.PAID, result.getStatus());
        assertEquals(DeliveryMethod.EXPRESS, result.getDeliveryMethod());

        BigDecimal expectedTotal = new BigDecimal("30.00");
        assertEquals(expectedTotal, result.getTotal());

    }

    @Test
    void getOrderStatusNotFound() {
        Mockito.when(repository.findOrderByUserAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Optional result = orderService.getOrderStatus(1L);
        assertTrue(result.isEmpty());

    }

    @Test
    public void addOrder() {
        User user = new User();
        user.setPhoneNumber("+491102022011");
        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("20.00"));
        product.setDiscountPrice(new BigDecimal("10.00"));
        Order newOrder = new Order();
        newOrder.setId(22L);
        newOrder.setDeliveryMethod(DeliveryMethod.EXPRESS);
        newOrder.setDeliveryAddress("Test address");
        newOrder.setStatus(null);
        newOrder.setContactPhone(null);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setPriceAtPurchase(null);
        orderItem.setOrder(newOrder);
        List<OrderItem> orderItems = Arrays.asList(orderItem);
        newOrder.setOrderItems(orderItems);

        Mockito.when(userService.getUserByEmail(Mockito.eq("customer@gmail.com"))).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(newOrder);
        OrderRequestDto dto = orderMapper.entityToDtoRequest(newOrder);
        orderService.addOrder(dto);
        Mockito.verify(repository).save(Mockito.argThat(order -> order.getStatus() == Status.PENDING && order.getContactPhone()
                .equals(user.getPhoneNumber()) && order.getOrderItems().get(0).getPriceAtPurchase()
                .equals(product.getDiscountPrice())));

    }


    @Test
    public void deleteOrderSuccess() {
        User user = new User();
        Long id = 55L;
        Order order = new Order();
        order.setId(id);
        order.setStatus(Status.PENDING);

        Mockito.when(userService.getUserByEmail(Mockito.eq("customer@gmail.com"))).thenReturn(Optional.of(user));
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(repository.findOrderByUserAndId(user, id)).thenReturn(Optional.of(order));

        orderService.deleteOrder(id);
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.eq(order));
        Mockito.verify(repository, Mockito.times(1)).findOrderByUserAndId(user, id);

    }

    @Test
    public void deleteOrderNotFound() {
        Long id = 55L;
        Mockito.when(repository.findOrderByUserAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.deleteOrder(id);
        });
    }

    @Test
    public void deleteOrderCannotDelete() {
        User user = new User();
        Order order = new Order();
        order.setId(11L);
        order.setStatus(Status.ON_THE_WAY);

        Mockito.when(userService.getUserByEmail(Mockito.eq("customer@gmail.com"))).thenReturn(Optional.of(user));
        Mockito.when(repository.findOrderByUserAndId(user, 11L)).thenReturn(Optional.of(order));
        assertThrows(CannotDeleteOrderException.class, () -> {
            orderService.deleteOrder(11L);
        });

        Mockito.verify(repository, Mockito.never()).delete(order);
        Mockito.verify(repository, Mockito.times(1)).findOrderByUserAndId(user, 11L);
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
