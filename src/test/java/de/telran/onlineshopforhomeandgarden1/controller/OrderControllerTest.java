package de.telran.onlineshopforhomeandgarden1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.onlineshopforhomeandgarden1.config.SecurityConfig;
import de.telran.onlineshopforhomeandgarden1.dto.request.OrderItemRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.OrderRequestDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderItemResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.dto.response.ProductResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.enums.DeliveryMethod;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.security.JwtProvider;
import de.telran.onlineshopforhomeandgarden1.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@Import(SecurityConfig.class)
class OrderControllerTest {

    @MockBean
    private OrderService service;
    private static OrderRepository repository;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    public void getOrdersHistory() throws Exception {
        List<OrderResponseDto> orders = new ArrayList<>();
        List<OrderItemResponseDto> orderItems1 = new ArrayList<>();
        orderItems1.add(new OrderItemResponseDto(2, BigDecimal.valueOf(7.00), new ProductResponseDto(1L, "Test Product", "Test Description", "Test Url")));
        OrderResponseDto order1 = new OrderResponseDto(1L, Instant.now(), orderItems1, "Test address", "Test contact number", DeliveryMethod.STANDARD, Status.PAID, Instant.now(), BigDecimal.valueOf(14.00));
        List<OrderItemResponseDto> orderItems2 = new ArrayList<>();
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
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    public void getOrderStatus() throws Exception {
        List<OrderItemResponseDto> orderItem = new ArrayList<>();
        orderItem.add(new OrderItemResponseDto(2, BigDecimal.valueOf(7.00), new ProductResponseDto(1L, "Test Product", "Test Description", "Test Url")));
        OrderResponseDto order = new OrderResponseDto(1L, Instant.now(), orderItem, "Test address", "Test contact number", DeliveryMethod.STANDARD, Status.PAID, Instant.now(), BigDecimal.valueOf(14.00));

        Mockito.when(service.getOrderStatus(1L)).thenReturn(Optional.of(order));
        mockMvc.perform(get("/orders/{orderId}", 1L).contentType("application/json")).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.PAID.toString()));

        Mockito.verify(service).getOrderStatus(1L);

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    public void getOrderStatusNotFound() throws Exception {
        Set<OrderItemResponseDto> orderItem = new LinkedHashSet<>();
        orderItem.add(new OrderItemResponseDto(2, BigDecimal.valueOf(7.00), new ProductResponseDto(1L, "Test Product", "Test Description", "Test Url")));
        Mockito.when(service.getOrderStatus(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/orders/{orderId}", 1L).contentType("application/json")).andExpect(status().isNotFound());
        Mockito.verify(service).getOrderStatus(1L);
    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    public void addOrder() throws Exception {
        List<OrderItemRequestDto> orderItem = new ArrayList<>();
        orderItem.add(new OrderItemRequestDto(2, "3L"));
        OrderRequestDto newOrder = new OrderRequestDto(3L, "Test address", "STANDARD", orderItem);
        String orderJs = new ObjectMapper().writeValueAsString(newOrder);
        Mockito.doNothing().when(service).addOrder(Mockito.any(OrderRequestDto.class));
        mockMvc.perform(post("/orders").contentType("application/json").content(orderJs))
                .andDo(print()).andExpect(status().isCreated());

        Mockito.verify(service).addOrder(Mockito.any(OrderRequestDto.class));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    public void deleteOrder() throws Exception {
        Long orderId = 4L;
        mockMvc.perform(delete("/orders/{id}", orderId)).andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(service).deleteOrder(eq(orderId));

    }

    @Test
    @WithMockUser(username = "Test user", roles = {"CUSTOMER"})
    public void deleteOrderNotFound() throws Exception {
        Long orderId = 5L;
        Mockito.doThrow(new EntityNotFoundException("Order is not found")).when(service).deleteOrder(eq(orderId));
        mockMvc.perform(delete("/orders/{id}", orderId)).andDo(print()).andExpect(status().isNotFound());
        Mockito.verify(service).deleteOrder(eq(orderId));
    }
}