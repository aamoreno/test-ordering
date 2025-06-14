package com.example.ordering.controller;

import com.example.ordering.dto.OrderDto;
import com.example.ordering.mapper.OrderMapper;
import com.example.ordering.model.Order;
import com.example.ordering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class WebController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public WebController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public String home() {
        return "redirect:/orders";
    }
    
    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        model.addAttribute("orders", orderDtos);
        return "orders/list";
    }
    
    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        if (orderOptional.isPresent()) {
            OrderDto orderDto = orderMapper.toDto(orderOptional.get());
            model.addAttribute("order", orderDto);
            return "orders/view";
        } else {
            return "redirect:/orders";
        }
    }
    
    @GetMapping("/orders/new")
    public String showCreateForm(Model model) {
        model.addAttribute("orderDto", new OrderDto());
        return "orders/create";
    }
    
    @PostMapping("/orders")
    public String createOrder(@Valid @ModelAttribute("orderDto") OrderDto orderDto, 
                             BindingResult result, 
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "orders/create";
        }
        
        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderService.createOrder(order);
        redirectAttributes.addFlashAttribute("successMessage", "Order created successfully with ID: " + savedOrder.getId());
        return "redirect:/orders";
    }
    
    @GetMapping("/orders/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        if (orderOptional.isPresent()) {
            OrderDto orderDto = orderMapper.toDto(orderOptional.get());
            model.addAttribute("orderDto", orderDto);
            return "orders/edit";
        } else {
            return "redirect:/orders";
        }
    }
    
    @PostMapping("/orders/{id}")
    public String updateOrder(@PathVariable Long id, 
                             @Valid @ModelAttribute("orderDto") OrderDto orderDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "orders/edit";
        }
        
        Optional<Order> orderOptional = orderService.getOrderById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            orderMapper.updateEntityFromDto(orderDto, order);
            orderService.updateOrder(id, order);
            redirectAttributes.addFlashAttribute("successMessage", "Order updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Order not found!");
        }
        
        return "redirect:/orders";
    }
    
    @GetMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Order not found!");
        }
        return "redirect:/orders";
    }
}
