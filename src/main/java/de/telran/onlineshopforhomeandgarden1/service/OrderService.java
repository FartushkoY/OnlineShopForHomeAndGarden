package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Periods;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.exception.CannotDeleteOrderException;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;



@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OrderMapper orderMapper;
    private final AuthService authService;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository repository, OrderMapper orderMapper, ProductRepository productRepository,AuthService authService, UserService userService) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.authService = authService;
        this.userService = userService;
        this.productRepository = productRepository;
    }


    public List<OrderResponseDto> getOrdersHistory()  {
        List<Order> orders = repository.findOrdersByUser(findUser());
        logger.debug("Retrieved {} orders for user ID {}", orders.size(), authService.getAuthInfo().getLogin());
        return orders.stream().map(this::getOrderResponseDtoWithTotal).toList();

    }

    private OrderResponseDto getOrderResponseDtoWithTotal(Order order) {
        OrderResponseDto dto = orderMapper.entityToDto(order);
        BigDecimal total = order.getOrderItems().stream().map(item -> item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotal(total);
        return dto;
    }

    public Optional<OrderResponseDto> getOrderStatus(Long id) {
        Optional<Order> order = repository.findOrderByUserAndId(findUser(), id);
        logger.debug("Retrieved {} order for user ID {}", id, authService.getAuthInfo().getLogin());
        return order.map(this::getOrderResponseDtoWithTotal);

    }

    public void addOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.dtoRequestToEntity(orderRequestDto);
        User user = findUser();
        order.setUser(user);
        order.setContactPhone(user.getPhoneNumber());
        order.setStatus(Status.PENDING);
        order.getOrderItems().forEach(orderItem -> {
            Long productId = orderItem.getProduct().getId();
            Product product = productRepository.findById(productId).get();
            BigDecimal priceAtPurchase;
            BigDecimal productDiscountPrice = product.getDiscountPrice();
            if(productDiscountPrice !=null){
                priceAtPurchase = productDiscountPrice;
            }else {
                priceAtPurchase = product.getPrice();
            }
            orderItem.setPriceAtPurchase(priceAtPurchase);
        });
        repository.save(order);
    }


    public void deleteOrder(Long id) {
      Order order = repository.findOrderByUserAndId(findUser(), id).orElseThrow(EntityNotFoundException::new);
        if (order.getStatus().equals(Status.PENDING)) {
            repository.delete(order);
            logger.debug("Order with id = {} deleted successfully.", id);
        } else {
            logger.debug("Order with id = {} is not in PENDING status. Delete operation failed.", id);
            throw new CannotDeleteOrderException("Order with id = " + id + " is not in PENDING status");
        }
    }

    private User findUser() {
        String email = authService.getAuthInfo().getLogin();
        return this.userService.getUserByEmail(email).get();
    }

    public List<Object> getRevenueReport(LocalDate startDate, Periods period, Integer duration, Periods detailing) {
        List<Object> detailingResult = new ArrayList<>();
        LocalDateTime localDateTimeStart = LocalDateTime.of(startDate, LocalTime.MIN);
        Instant instantStart = localDateTimeStart.toInstant(ZoneOffset.UTC);
        LocalDate localDateEnd = LocalDate.now();
        LocalDateTime localDateTimeEnd = LocalDateTime.now();
        Instant instantEnd = Instant.now();
        String detailingPeriod = "";
        List<Object> result = new ArrayList<>();

        switch (detailing) {
            case HOUR:
                detailingPeriod = "%Y %m %d %H";
                break;
            case DAY:
                detailingPeriod = "%Y %m %d";
                break;
        }

        if (period.equals(Periods.DAY)) {
            localDateEnd = startDate.plusDays(duration);
            instantEnd = getInstant(localDateEnd);
            detailingResult = repository.getRevenueReportDetailedByDayOrHour(instantStart, instantEnd, detailingPeriod);
        }

        if (period.equals(Periods.YEAR)) {
            localDateEnd = startDate.plusYears(duration);
            instantEnd = getInstant(localDateEnd);
        }

        if (period.equals(Periods.MONTH)) {
            localDateEnd = startDate.plusMonths(duration);
            instantEnd = getInstant(localDateEnd);
        }

        if (period.equals(Periods.YEAR) || period.equals(Periods.MONTH)) {
            switch (detailing) {
                case WEEK:
                    detailingResult = repository.getRevenueReportDetailedByWeek(instantStart, instantEnd);
                    break;
                case MONTH:
                    detailingResult = repository.getRevenueReportDetailedByMonth(instantStart, instantEnd);
                    break;
                case YEAR:
                    detailingResult = repository.getRevenueReportDetailedByYear(instantStart, instantEnd);
                    break;
                default:
                    detailingResult = repository.getRevenueReportDetailedByDayOrHour(instantStart, instantEnd, detailingPeriod);
            }
        }
        BigDecimal totalResult = repository.getTotalResult(instantStart, instantEnd);
        result.add(totalResult);
        result.add(detailingResult);

        return result;
    }


    private Instant getInstant(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN).toInstant(ZoneOffset.UTC);
    }
}
