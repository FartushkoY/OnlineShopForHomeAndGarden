package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.exception.CannotDeleteOrderException;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import de.telran.onlineshopforhomeandgarden1.security.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final UserService userService;


    @Autowired
    public OrderService(OrderRepository repository, OrderMapper orderMapper, ProductRepository productRepository,AuthService authService, UserService userService) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.authService = authService;
        this.userService = userService;
    }

    public Set<OrderResponseDto> getOrdersHistory()  {
        Set<Order> orders = repository.findOrdersByUserId(findUser().getId());
        logger.info("Retrieved {} orders for user ID {}", orders.size(), authService.getAuthInfo().getLogin());
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
        order.setUser(findUser());
        repository.save(order);
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
            throw new CannotDeleteOrderException ("Order with id = " + id + " is not in PENDING status");
        }


    }

    private User findUser() {
        String email = authService.getAuthInfo().getLogin();
        return this.userService.getUserByEmail(email).get();
    }
}
