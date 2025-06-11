package com.example.ordering.service;

import com.example.ordering.model.Order;
import com.example.ordering.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
        return orderRepository.save(order);
    }

    public Optional<Order> updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setProductId(orderDetails.getProductId());
                    existingOrder.setQuantity(orderDetails.getQuantity());
                    existingOrder.setStatus(orderDetails.getStatus());
                    return orderRepository.save(existingOrder);
                });
    }

    public Optional<Order> cancelOrder(Long id) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus("CANCELLED");
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
