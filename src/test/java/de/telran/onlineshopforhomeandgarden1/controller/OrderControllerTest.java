package de.telran.onlineshopforhomeandgarden1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.dto.request.OrderItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getOrdersHistory() throws Exception {
        Set<OrderResponseDto> orders = new LinkedHashSet<>();
        Set<OrderItemResponseDto> orderItems1 = new LinkedHashSet<>();
        orderItems1.add(new OrderItemResponseDto(2, BigDecimal.valueOf(7.00), new ProductResponseDto(1L, "Test Product", "Test Description", "Test Url")));
        OrderResponseDto order1 = new OrderResponseDto(1L, Instant.now(), orderItems1, "Test address", "Test contact number", DeliveryMethod.STANDARD, Status.PAID, Instant.now(), BigDecimal.valueOf(14.00));
        Set<OrderItemResponseDto> orderItems2 = new LinkedHashSet<>();
        orderItems2.add(new OrderItemResponseDto(1, BigDecimal.valueOf(10.00), new ProductResponseDto(2L, "Test Product2", "Test Description2", "Test Url2")));
        OrderResponseDto order2 = new OrderResponseDto(2L, Instant.now(), orderItems2, "Test address2", "Test contact number2", DeliveryMethod.STANDARD, Status.PAID, Instant.now(), BigDecimal.valueOf(10.00));

        orders.add(order1);
        orders.add(order2);

        Mockito.when(service.getOrdersHistory()).thenReturn (orders);

        mockMvc.perform(get("/orders/history").contentType("application/json"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(orders.size()));

        Mockito.verify(service).getOrdersHistory();

    }

    @Test
    public void getOrderStatus() throws Exception {
        Set<OrderItemResponseDto> orderItem = new LinkedHashSet<>();
        orderItem.add(new OrderItemResponseDto(2, BigDecimal.valueOf(7.00), new ProductResponseDto(1L, "Test Product", "Test Description", "Test Url")));
        OrderResponseDto order = new OrderResponseDto(1L, Instant.now(), orderItem, "Test address", "Test contact number", DeliveryMethod.STANDARD, Status.PAID, Instant.now(), BigDecimal.valueOf(14.00));

        Mockito.when(service.getOrderStatus(1L)).thenReturn(Optional.of(order));
        mockMvc.perform(get("/orders/{orderId}", 1L).contentType("application/json")).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.PAID.toString()));

        Mockito.verify(service).getOrderStatus(1L);

    }

    @Test
    public void getOrderStatusNotFound() throws Exception {
        Set<OrderItemResponseDto> orderItem = new LinkedHashSet<>();
        orderItem.add(new OrderItemResponseDto(2, BigDecimal.valueOf(7.00), new ProductResponseDto(1L, "Test Product", "Test Description", "Test Url")));
        Mockito.when(service.getOrderStatus(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/orders/{orderId}", 1L).contentType("application/json")).andExpect(status().isNotFound());
        Mockito.verify(service).getOrderStatus(1L);
    }

    @Test
    public void addOrder() throws Exception {
        Set<OrderItemRequestDto> orderItem = new LinkedHashSet<>();
        orderItem.add(new OrderItemRequestDto(2, "3L"));
        OrderRequestDto newOrder = new OrderRequestDto(3L, "Test address", "STANDARD", orderItem);
        String orderJs = new ObjectMapper().writeValueAsString(newOrder);
        Mockito.doNothing().when(service).addOrder(Mockito.any(OrderRequestDto.class));
        mockMvc.perform(post("/orders").contentType("application/json").content(orderJs))
                .andDo(print()).andExpect(status().isCreated());

        Mockito.verify(service).addOrder(Mockito.any(OrderRequestDto.class));

    }

    @Test
    public void deleteOrder() throws Exception {
        Long orderId = 4L;
        Mockito.when(service.deleteOrder(eq(orderId))).thenReturn(Optional.of(new Order()));
        mockMvc.perform(delete("/orders/{id}", orderId)).andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(service).deleteOrder(eq(orderId));

    }

    @Test
    public void deleteOrderNotFound() throws Exception {
        Long orderId = 5L;
        Mockito.when(service.deleteOrder(eq(orderId))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/orders/{id}", orderId)).andDo(print())
                .andExpect(status().isNotFound());
        Mockito.verify(service).deleteOrder(eq(orderId));

    }
}