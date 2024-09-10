package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@Slf4j

public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {this.service = service;}

    @GetMapping("/history")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity <Set<OrderResponseDto>> getOrdersHistory() {
        return new ResponseEntity<>(service.getOrdersHistory(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrderResponseDto> getOrderStatus(@PathVariable Long orderId) {
        Optional<OrderResponseDto> order = service.getOrderStatus(orderId);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Void> addOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        service.addOrder(orderRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Optional<Order> order = service.deleteOrder(id);
        return new ResponseEntity<>(order.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
