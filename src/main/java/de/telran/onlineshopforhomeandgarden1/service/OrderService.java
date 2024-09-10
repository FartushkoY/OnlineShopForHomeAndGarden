package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Periods;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.exception.CannotDeleteOrderException;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;


    @Autowired
    public OrderService(OrderRepository repository, OrderMapper orderMapper, ProductRepository productRepository) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
    }

    public Set<OrderResponseDto> getOrdersHistory() {
        Set<Order> orders = repository.findOrdersByUserId(this.getAuthenticatedUser().getId());
        logger.info("Retrieved {} orders for user ID {}", orders.size(), this.getAuthenticatedUser().getId());
        return orders.stream().map(order -> {
            OrderResponseDto dto = orderMapper.entityToDto(order);
            BigDecimal total = order.getOrderItems().stream().map(item -> item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotal(total);
            return dto;
        }).collect(Collectors.toSet());

    }

    public Optional<OrderResponseDto> getOrderStatus(Long id) {
        Optional<Order> order = repository.findById(id);
        logger.debug("Order retrieved from DB: id =  {}", () -> order.orElse(null));
        return order.map(orderMapper::entityToDto);

    }


    public void addOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.dtoRequestToEntity(orderRequestDto);
        order.setUser(this.getAuthenticatedUser());
        repository.save(order);

    }

    private User getAuthenticatedUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    public Optional<Order> deleteOrder(Long id) {
        Optional<Order> order = repository.findById(id);
        if (order.isEmpty()) {
            logger.warn("Order with id = {} not found. Delete operation failed.", id);
            return Optional.empty();
        }
        Order existingOrder = order.get();
        if (existingOrder.getStatus().equals(Status.PENDING)) {
            repository.delete(existingOrder);
            logger.info("Order with id = {} deleted successfully.", id);
            return order;
        } else {
            logger.info("Order with id = {} is not in PENDING status. Delete operation failed.", id);
            throw new CannotDeleteOrderException("Order with id = " + id + " is not in PENDING status");
        }
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
