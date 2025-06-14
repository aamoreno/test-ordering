package com.example.ordering.controller;

import com.example.ordering.dto.OrderDto;
import com.example.ordering.mapper.OrderMapper;
import com.example.ordering.model.Order;
import com.example.ordering.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

@WebMvcTest(controllers = OrderController.class)
@Import(OrderMapper.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Test
    public void testGetOrderById() throws Exception {
        // Create a test Order entity
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId("customer123");
        order.setProductId("product456");
        order.setQuantity(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        // Create a test OrderDto that would be returned by the mapper
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCustomerId("customer123");
        orderDto.setProductId("product456");
        orderDto.setQuantity(2);
        orderDto.setStatus("PENDING");
        orderDto.setOrderDate(order.getOrderDate());

        // Mock the service and mapper behavior
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

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
        // Create test Order entities
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

        // Create corresponding DTOs
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setId(1L);
        orderDto1.setCustomerId("customer123");
        orderDto1.setProductId("product456");
        orderDto1.setQuantity(2);
        orderDto1.setStatus("PENDING");

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setId(2L);
        orderDto2.setCustomerId("customer789");
        orderDto2.setProductId("product012");
        orderDto2.setQuantity(1);
        orderDto2.setStatus("SHIPPED");

        // Mock the service and mapper behavior
        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));
        when(orderMapper.toDtoList(Arrays.asList(order1, order2))).thenReturn(Arrays.asList(orderDto1, orderDto2));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("SHIPPED"));
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Create input DTO (from request)
        OrderDto inputDto = new OrderDto();
        inputDto.setCustomerId("customer123");
        inputDto.setProductId("product456");
        inputDto.setQuantity(2);

        // Create entity that would be passed to the service
        Order order = new Order();
        order.setCustomerId("customer123");
        order.setProductId("product456");
        order.setQuantity(2);

        // Create created entity (with ID) that would be returned by the service
        Order createdOrder = new Order();
        createdOrder.setId(1L);
        createdOrder.setCustomerId("customer123");
        createdOrder.setProductId("product456");
        createdOrder.setQuantity(2);
        createdOrder.setStatus("PENDING");
        createdOrder.setOrderDate(LocalDateTime.now());

        // Create DTO that would be returned in the response
        OrderDto responseDto = new OrderDto();
        responseDto.setId(1L);
        responseDto.setCustomerId("customer123");
        responseDto.setProductId("product456");
        responseDto.setQuantity(2);
        responseDto.setStatus("PENDING");
        responseDto.setOrderDate(createdOrder.getOrderDate());

        // Mock the mapper and service behavior
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderService.createOrder(order)).thenReturn(createdOrder);
        when(orderMapper.toDto(createdOrder)).thenReturn(responseDto);

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

    @Test
    public void testUpdateOrder() throws Exception {
        // Create input DTO for the update
        OrderDto updateDto = new OrderDto();
        updateDto.setCustomerId("updatedCustomer");
        updateDto.setProductId("updatedProduct");
        updateDto.setQuantity(5);

        // Create entity that would be passed to the service
        Order updateOrder = new Order();
        updateOrder.setCustomerId("updatedCustomer");
        updateOrder.setProductId("updatedProduct");
        updateOrder.setQuantity(5);

        // Create existing entity from the database
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setCustomerId("customer123");
        existingOrder.setProductId("product456");
        existingOrder.setQuantity(2);
        existingOrder.setStatus("PENDING");
        existingOrder.setOrderDate(LocalDateTime.now());

        // Create updated entity after saving changes
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomerId("updatedCustomer");
        updatedOrder.setProductId("updatedProduct");
        updatedOrder.setQuantity(5);
        updatedOrder.setStatus("PENDING");
        updatedOrder.setOrderDate(existingOrder.getOrderDate());

        // Create DTO that would be returned in the response
        OrderDto responseDto = new OrderDto();
        responseDto.setId(1L);
        responseDto.setCustomerId("updatedCustomer");
        responseDto.setProductId("updatedProduct");
        responseDto.setQuantity(5);
        responseDto.setStatus("PENDING");
        responseDto.setOrderDate(updatedOrder.getOrderDate());

        // Mock the mapper and service behavior
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(updateOrder);
        when(orderService.updateOrder(anyLong(), any(Order.class))).thenReturn(Optional.of(existingOrder)).thenReturn(Optional.of(updatedOrder));
        when(orderMapper.toDto(updatedOrder)).thenReturn(responseDto);

        mockMvc.perform(put("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":\"updatedCustomer\",\"productId\":\"updatedProduct\",\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value("updatedCustomer"))
                .andExpect(jsonPath("$.productId").value("updatedProduct"))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        // Mock the service behavior
        when(orderService.deleteOrder(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }

}
