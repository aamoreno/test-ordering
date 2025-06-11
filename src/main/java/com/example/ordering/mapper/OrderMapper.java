package com.example.ordering.mapper;

import com.example.ordering.dto.OrderDto;
import com.example.ordering.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setProductId(order.getProductId());
        dto.setQuantity(order.getQuantity());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }
    
    public List<OrderDto> toDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Order toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        
        Order order = new Order();
        order.setId(dto.getId());
        order.setCustomerId(dto.getCustomerId());
        order.setProductId(dto.getProductId());
        order.setQuantity(dto.getQuantity());
        
        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }
        
        if (dto.getOrderDate() != null) {
            order.setOrderDate(dto.getOrderDate());
        }
        
        return order;
    }
    
    public void updateEntityFromDto(OrderDto dto, Order order) {
        if (dto == null || order == null) {
            return;
        }
        
        if (dto.getCustomerId() != null) {
            order.setCustomerId(dto.getCustomerId());
        }
        
        if (dto.getProductId() != null) {
            order.setProductId(dto.getProductId());
        }
        
        if (dto.getQuantity() != null) {
            order.setQuantity(dto.getQuantity());
        }
        
        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }
        
        if (dto.getOrderDate() != null) {
            order.setOrderDate(dto.getOrderDate());
        }
    }
}
