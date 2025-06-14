package com.example.ordering.service;

import com.example.ordering.model.Order;
import com.example.ordering.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {    
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;   
    
    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomerId("customer123");
        testOrder.setProductId("product456");
        testOrder.setQuantity(2);
        testOrder.setStatus("PENDING");
        
        // Set the mocked RestTemplate into the service
        ReflectionTestUtils.setField(orderService, "restTemplate", restTemplate);
        // Set a test URL for the shipping service
        ReflectionTestUtils.setField(orderService, "shippingServiceUrl", "http://test-shipping-service");
    }

    @Test
    void testGetAllOrders() {
        // Given
        Order order2 = new Order();
        order2.setId(2L);
        order2.setCustomerId("customer789");
        List<Order> expectedOrders = Arrays.asList(testOrder, order2);
        
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        // When
        List<Order> actualOrders = orderService.getAllOrders();

        // Then
        assertEquals(2, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When
        Optional<Order> foundOrder = orderService.getOrderById(1L);

        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals(testOrder, foundOrder.get());
        verify(orderRepository, times(1)).findById(1L);
    }    
    
    @Test
    void testCreateOrder() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(restTemplate.postForObject(
                eq("http://test-shipping-service/api/shipments"),
                any(),
                eq(Object.class)
        )).thenReturn(new Object()); // Return a dummy response

        // When
        Order createdOrder = orderService.createOrder(testOrder);

        // Then
        assertNotNull(createdOrder);
        assertEquals(testOrder, createdOrder);
        
        // Verify repository was called to save the order
        verify(orderRepository, times(1)).save(testOrder);
        
        // Verify RestTemplate was called to create the shipment
        verify(restTemplate, times(1)).postForObject(
                eq("http://test-shipping-service/api/shipments"),
                any(),
                eq(Object.class)
        );
    }
    
    @Test
    void testCreateOrder_ShipmentCreationFails() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(restTemplate.postForObject(
                eq("http://test-shipping-service/api/shipments"),
                any(),
                eq(Object.class)
        )).thenThrow(new RuntimeException("Service unavailable"));

        // When
        Order createdOrder = orderService.createOrder(testOrder);

        // Then
        // The order should still be created even if shipment creation fails
        assertNotNull(createdOrder);
        assertEquals(testOrder, createdOrder);
        
        // Verify repository was called to save the order
        verify(orderRepository, times(1)).save(testOrder);
        
        // Verify RestTemplate was called to create the shipment
        verify(restTemplate, times(1)).postForObject(
                eq("http://test-shipping-service/api/shipments"),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void testUpdateOrder() {
        // Given
        Order updatedOrder = new Order();
        updatedOrder.setProductId("newProduct");
        updatedOrder.setQuantity(5);
        updatedOrder.setStatus("SHIPPED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<Order> result = orderService.updateOrder(1L, updatedOrder);

        // Then
        assertTrue(result.isPresent());
        assertEquals("newProduct", result.get().getProductId());
        assertEquals(5, result.get().getQuantity());
        assertEquals("SHIPPED", result.get().getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testDeleteOrder() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        doNothing().when(orderRepository).delete(any(Order.class));

        // When
        boolean result = orderService.deleteOrder(1L);

        // Then
        assertTrue(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).delete(testOrder);
    }
}
