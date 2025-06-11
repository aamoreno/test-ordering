package com.example.ordering.controller;

import com.example.ordering.model.Order;
import com.example.ordering.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId("customer123");
        order.setProductId("product456");
        order.setQuantity(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value("customer123"))
                .andExpect(jsonPath("$.productId").value("product456"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testGetAllOrders() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setCustomerId("customer123");
        order1.setProductId("product456");
        order1.setQuantity(2);
        order1.setStatus("PENDING");

        Order order2 = new Order();
        order2.setId(2L);
        order2.setCustomerId("customer789");
        order2.setProductId("product012");
        order2.setQuantity(1);
        order2.setStatus("SHIPPED");

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("SHIPPED"));
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId("customer123");
        order.setProductId("product456");
        order.setQuantity(2);
        order.setStatus("PENDING");

        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":\"customer123\",\"productId\":\"product456\",\"quantity\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value("customer123"))
                .andExpect(jsonPath("$.productId").value("product456"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
