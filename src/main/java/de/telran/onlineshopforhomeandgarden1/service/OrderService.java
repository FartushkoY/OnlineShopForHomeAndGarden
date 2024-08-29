package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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

    public Set<OrderResponseDto> getOrdersHistory()  {
        Set<Order> orders = repository.findOrdersByUserId(this.getAuthenticatedUser().getId());
        logger.info("Retrieved {} orders for user ID {}", orders.size(), this.getAuthenticatedUser().getId());
        return orderMapper.entityListToDto(orders);
    }

    public Optional<OrderResponseDto> getOrderStatus(Long id) {
        Optional<Order> order = repository.findById(id);
        logger.debug("Order retrieved from DB: id =  {}", () -> order.orElse(null));
        return order.map(orderMapper::entityToDto);

    }


    public OrderRequestDto addOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.dtoRequestToEntity(orderRequestDto);
        order.setUser(this.getAuthenticatedUser());
        Order created = repository.save(order);
        logger.info("Order with id = {} created", created.getId());
        return orderMapper.entityToDtoRequest(created);
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
            throw new RuntimeException("Order with id = " + id + " is not in PENDING status");
        }
    }
}
