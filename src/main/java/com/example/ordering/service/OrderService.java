package com.example.ordering.service;

import com.example.ordering.model.Order;
import com.example.ordering.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;    private final RestTemplate restTemplate;
    
    @Value("${shipping.service.url:http://localhost:8081}")
    private String shippingServiceUrl;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public Order createOrder(Order order) {
        // Save the order first
        Order savedOrder = orderRepository.save(order);
        
        // Create a shipment for the order
        try {
            createShipment(savedOrder);
        } catch (Exception e) {
            // Log the error but don't fail the order creation
            System.err.println("Failed to create shipment for order " + savedOrder.getId() + ": " + e.getMessage());
        }
        
        return savedOrder;
    }

    private void createShipment(Order order) {
        String url = shippingServiceUrl + "/api/shipments";
        
        // Create the request payload for the shipping service
        Map<String, Object> shipmentRequest = new HashMap<>();
        shipmentRequest.put("orderId", order.getId());
        shipmentRequest.put("customerId", order.getCustomerId());
        shipmentRequest.put("status", "WAITING_FOR_SHIPMENT");
        
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Create the HTTP entity with headers and body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(shipmentRequest, headers);
        
        // Send the request to create a shipment
        restTemplate.postForObject(url, requestEntity, Object.class);
    }

    public Optional<Order> updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setProductId(orderDetails.getProductId());
                    existingOrder.setQuantity(orderDetails.getQuantity());
                    existingOrder.setStatus(orderDetails.getStatus());
                    existingOrder.setTrackingNumber(orderDetails.getTrackingNumber());
                    return orderRepository.save(existingOrder);
                });
    }

    public boolean deleteOrder(Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.delete(order);
                    return true;
                })
                .orElse(false);
    }
}
