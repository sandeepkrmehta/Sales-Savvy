package com.salesSavvy.service;

import org.springframework.stereotype.Service;
import com.salesSavvy.entity.Orders;
import com.salesSavvy.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Orders placeOrder(Orders order) {
        order.setStatus("CREATED"); // default status
        return orderRepository.save(order);
    }

    @Override
    public List<Orders> getAllOrders() {
        // Use optimized query with JOIN FETCH
        return orderRepository.findAll();
    }

    @Override
    public List<Orders> getOrdersByUserId(Long userId) {
        // Already optimized with JOIN FETCH in repository
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Optional<Orders> getOrderByDbId(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Optional<Orders> getOrderByRazorpayId(String razorpayOrderId) {
        // Already optimized with JOIN FETCH in repository
        return orderRepository.findByRazorpayOrderId(razorpayOrderId);
    }

    @Override
    public String cancelOrder(String razorpayOrderId) {
        Optional<Orders> optOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId);
        if (optOrder.isPresent()) {
            Orders order = optOrder.get();
            if (!"PAID".equalsIgnoreCase(order.getStatus())) {
                order.setStatus("CANCELLED");
                orderRepository.save(order);
                return "Order cancelled successfully";
            } else {
                return "Cannot cancel a paid order";
            }
        }
        return "Order not found";
    }

    @Override
    public String updateOrderStatus(String razorpayOrderId, String status) {
        Optional<Orders> optOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId);
        if (optOrder.isPresent()) {
            Orders order = optOrder.get();
            order.setStatus(status.toUpperCase());
            orderRepository.save(order);
            return "Order status updated to " + status;
        }
        return "Order not found";
    }
}