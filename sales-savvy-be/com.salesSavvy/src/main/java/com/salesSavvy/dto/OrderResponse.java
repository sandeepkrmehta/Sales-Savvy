package com.salesSavvy.dto;

import com.salesSavvy.entity.Orders;
import java.util.List;

public class OrderResponse {

    private String razorpayOrderId;
    private Double amount;        // in INR
    private String status;        // CREATED | PAID | CANCELLED
    private Long userId;          // just the user ID
    private List<String> items;   // product names + quantity snapshot

    public OrderResponse(Orders order) {
        this.razorpayOrderId = order.getRazorpayOrderId();
        this.amount = order.getAmount() / 100.0; // convert paise to INR
        this.status = order.getStatus();
        this.userId = order.getUser() != null ? order.getUser().getId() : null;

        // map CartItems to readable strings
        if (order.getItems() != null) {
            this.items = order.getItems().stream()
                              .map(ci -> ci.getProduct().getName() + " x" + ci.getQuantity())
                              .toList();
        }
    }

    // ---------------- Getters & Setters ----------------
    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
