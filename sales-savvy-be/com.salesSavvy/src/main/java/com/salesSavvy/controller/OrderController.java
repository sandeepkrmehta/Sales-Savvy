package com.salesSavvy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.salesSavvy.dto.OrderResponse;
import com.salesSavvy.entity.Orders;
import com.salesSavvy.exception.BadRequestException;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.service.OrderService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Customer: place order
    @PostMapping("/place")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody Orders order) {
        
        if (order.getAmount() == null || order.getAmount().doubleValue() <= 0) {
            throw new BadRequestException("Order amount must be greater than 0");
        }
        
        if (order.getUser() == null) {
            throw new BadRequestException("User information is required");
        }
        
        Orders placedOrder = orderService.placeOrder(order);
        logger.info("Order placed: {}", placedOrder.getRazorpayOrderId());
        
        return ResponseEntity.ok(new OrderResponse(placedOrder));
    }

    // Customer: view own orders
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {
        
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId)
            .stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
        
        logger.info("Retrieved {} orders for user: {}", orders.size(), userId);
        return ResponseEntity.ok(orders);
    }

    // Customer: cancel order by Razorpay ID
    @PutMapping("/user/cancel/{razorpayOrderId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<String> cancelOrder(@PathVariable String razorpayOrderId) {
        
        String result = orderService.cancelOrder(razorpayOrderId);
        
        if (result.contains("not found")) {
            throw new ResourceNotFoundException("Order", "razorpayOrderId", razorpayOrderId);
        }
        
        if (result.contains("Cannot cancel")) {
            throw new BadRequestException(result);
        }
        
        logger.info("Order cancelled: {}", razorpayOrderId);
        return ResponseEntity.ok(result);
    }

    // Admin: view all orders
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        
        List<OrderResponse> orders = orderService.getAllOrders()
            .stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
        
        logger.info("Admin retrieved all orders. Total: {}", orders.size());
        return ResponseEntity.ok(orders);
    }

    // Admin: update order status by Razorpay ID
    @PutMapping("/admin/update-status/{razorpayOrderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable String razorpayOrderId,
            @RequestParam String status) {
        
        if (status == null || status.isBlank()) {
            throw new BadRequestException("Status cannot be empty");
        }
        
        // Validate status values
        String upperStatus = status.toUpperCase();
        if (!List.of("CREATED", "PAID", "CANCELLED", "SHIPPED", "DELIVERED")
                .contains(upperStatus)) {
            throw new BadRequestException("Invalid status value: " + status);
        }
        
        String result = orderService.updateOrderStatus(razorpayOrderId, upperStatus);
        
        if (result.contains("not found")) {
            throw new ResourceNotFoundException("Order", "razorpayOrderId", razorpayOrderId);
        }
        
        logger.info("Order status updated: {} to {}", razorpayOrderId, upperStatus);
        return ResponseEntity.ok(result);
    }

    // Get order by DB ID (admin/internal use)
    @GetMapping("/db/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> getOrderByDbId(@PathVariable Long id) {
        
        Orders order = orderService.getOrderByDbId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        
        return ResponseEntity.ok(new OrderResponse(order));
    }

    // Get order by Razorpay Order ID (customer/payment use)
    @GetMapping("/razorpay/{razorpayOrderId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<OrderResponse> getOrderByRazorpayId(@PathVariable String razorpayOrderId) {
        
        Orders order = orderService.getOrderByRazorpayId(razorpayOrderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "razorpayOrderId", razorpayOrderId));
        
        return ResponseEntity.ok(new OrderResponse(order));
    }
}