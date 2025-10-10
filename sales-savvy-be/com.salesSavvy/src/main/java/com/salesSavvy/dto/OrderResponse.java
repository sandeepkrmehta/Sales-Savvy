package com.salesSavvy.dto;

import com.salesSavvy.entity.Orders;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private String razorpayOrderId;
    private BigDecimal amount;        // in INR (converted from paise)
    private String currency;
    private String status;            // CREATED | PAID | CANCELLED | SHIPPED | DELIVERED
    private String paymentId;
    private Long userId;              
    private String username;
    private List<OrderItemResponse> items;
    private int itemCount;

    public OrderResponse() {}

    public OrderResponse(Orders order) {
        this.id = order.getId();
        this.razorpayOrderId = order.getRazorpayOrderId();
        
        // Convert paise to INR
        this.amount = order.getAmount().divide(BigDecimal.valueOf(100));
        
        this.currency = order.getCurrency();
        this.status = order.getStatus();
        this.paymentId = order.getPaymentId();
        
        if (order.getUser() != null) {
            this.userId = order.getUser().getId();
            this.username = order.getUser().getUsername();
        }

        // Map CartItems to readable order items
        if (order.getItems() != null) {
            this.items = order.getItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
            this.itemCount = order.getItems().size();
        } else {
            this.itemCount = 0;
        }
    }

    // ---------------- Getters & Setters ----------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}