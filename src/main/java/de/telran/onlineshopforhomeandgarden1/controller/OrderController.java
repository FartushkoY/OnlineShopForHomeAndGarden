package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public List<OrderResponseDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderStatus(@PathVariable Long orderId) {
        Optional<OrderResponseDto> order = service.getOrderStatus(orderId);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<OrderRequestDto> addOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        try {
        OrderRequestDto created = service.addOrder(orderRequestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
