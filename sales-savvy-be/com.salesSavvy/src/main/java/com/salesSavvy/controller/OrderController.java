package com.salesSavvy.controller;

import org.springframework.web.bind.annotation.*;

import com.salesSavvy.dto.OrderResponse;
import com.salesSavvy.entity.Orders;
import com.salesSavvy.service.OrderService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Customer: place order
    @PostMapping("/place")
    public Orders placeOrder(@RequestBody Orders order) {
        return orderService.placeOrder(order);
    }

    // Customer: view own orders
    @GetMapping("/user/{userId}")
    public List<OrderResponse> getUserOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId)
                           .stream().map(OrderResponse::new).toList();
    }


    // Customer: cancel order by Razorpay ID
    @PutMapping("/user/cancel/{razorpayOrderId}")
    public String cancelOrder(@PathVariable String razorpayOrderId) {
        return orderService.cancelOrder(razorpayOrderId);
    }

    // Admin: view all orders
    @GetMapping("/all")
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Admin: update order status by Razorpay ID
    @PutMapping("/admin/update-status/{razorpayOrderId}")
    public String updateOrderStatus(@PathVariable String razorpayOrderId,
                                    @RequestParam String status) {
        return orderService.updateOrderStatus(razorpayOrderId, status);
    }

    // Get order by DB ID (admin/internal use)
    @GetMapping("/db/{id}")
    public Optional<Orders> getOrderByDbId(@PathVariable Long id) {
        return orderService.getOrderByDbId(id);
    }

    // Get order by Razorpay Order ID (customer/payment use)
    @GetMapping("/razorpay/{razorpayOrderId}")
    public Optional<Orders> getOrderByRazorpayId(@PathVariable String razorpayOrderId) {
        return orderService.getOrderByRazorpayId(razorpayOrderId);
    }
}
