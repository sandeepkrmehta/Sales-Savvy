package com.salesSavvy.service;

import com.salesSavvy.entity.Orders;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Orders placeOrder(Orders order);                // customer places order

    List<Orders> getAllOrders();                    // admin sees all orders

    List<Orders> getOrdersByUserId(Long userId);    // customer sees own orders

    Optional<Orders> getOrderByDbId(Long id);       // lookup by DB primary key

    Optional<Orders> getOrderByRazorpayId(String razorpayOrderId); // lookup by Razorpay ID

    String cancelOrder(String razorpayOrderId);     // customer cancels unpaid order

    String updateOrderStatus(String razorpayOrderId, String status); // admin updates status
}
