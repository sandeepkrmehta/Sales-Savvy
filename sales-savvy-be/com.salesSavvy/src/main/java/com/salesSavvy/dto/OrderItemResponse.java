package com.salesSavvy.dto;

import com.salesSavvy.entity.CartItem;
import java.math.BigDecimal;

public class OrderItemResponse {
    
    private Long productId;
    private String productName;
    private String productPhoto;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;

    public OrderItemResponse() {}

    public OrderItemResponse(CartItem item) {
        if (item.getProduct() != null) {
            this.productId = item.getProduct().getId();
            this.productName = item.getProduct().getName();
            this.productPhoto = item.getProduct().getPhoto();
            this.price = item.getProduct().getPrice();
        }
        this.quantity = item.getQuantity();
        
        // Calculate subtotal
        if (this.price != null) {
            this.subtotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}