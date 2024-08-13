package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.OrderDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OrderMapper orderMapper;


    @Autowired
    public OrderService(OrderRepository repository, OrderMapper orderMapper) {
        this.repository = repository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getAll() {
        List<Order> orders = repository.findAll();
        return orderMapper.entityListToDto(orders);
    }

    public Optional<OrderDto> getOrderStatus(Long id) {
        Optional<Order> order = repository.findById(id);
        logger.debug("Order retrieved from DB: id =  {}", () -> order.orElse(null));
        return order.map(orderMapper::entityToDto);

    }

}
