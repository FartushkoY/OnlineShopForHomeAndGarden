package de.telran.onlineshopforhomeandgarden1.controller;

import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.enums.Periods;
import de.telran.onlineshopforhomeandgarden1.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@Slf4j
@Tag(name = "Order Controller", description = "Operations related to customer orders")
public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {this.service = service;}

    @GetMapping("/history")
    @Operation(summary = "Retrieve all orders for the authenticated customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity <Set<OrderResponseDto>> getOrdersHistory() {
        return new ResponseEntity<>(service.getOrdersHistory(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Retrieve a specific order by its ID for the authenticated customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrderResponseDto> getOrderStatus(@PathVariable Long orderId) {
        Optional<OrderResponseDto> order = service.getOrderStatus(orderId);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/revenueReport")
    @Operation(summary="Retrieve a revenue report for a specific period",
               description = "Fetches a revenue report, covering a specified period (days, months, years) with optional grouping by hours, days, weeks, or months. Only administrators are authorised to perform this action.")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public List<Object> getRevenueReport(@RequestParam LocalDate startDate,
                                             @RequestParam String period,
                                             @RequestParam(required = false) Integer duration,
                                             @RequestParam(required = false) String detailing) {
        if (detailing == null) {
            detailing = period;
        }
         return service.getRevenueReport(startDate, Periods.valueOf(period),  duration, Periods.valueOf(detailing));


    }

    @PostMapping
    @Operation(summary = "Add a new order for the authenticated customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Void> addOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        service.addOrder(orderRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing order of the authenticated customer identified by its ID")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Optional<Order> order = service.deleteOrder(id);
        return new ResponseEntity<>(order.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
