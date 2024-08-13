package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.OrderDto;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Validated
@Slf4j

public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {this.service = service;}

    @GetMapping
    public List<OrderDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderStatus(@PathVariable Long orderId) {
        Optional<OrderDto> order = service.getOrderStatus(orderId);
        if (order.isPresent()) {
            log.info("Get order by id: {} successfully ", orderId);
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            log.info("Get book detail by id: {} not found ", orderId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
