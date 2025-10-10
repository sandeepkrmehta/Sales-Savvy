package com.salesSavvy.dto;

import com.salesSavvy.entity.Cart;
import com.salesSavvy.entity.CartItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {
    
    private Long cartId;
    private String username;
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
    private int totalItems;

    public CartResponse() {}

    public CartResponse(Cart cart) {
        this.cartId = cart.getId();
        this.username = cart.getUser() != null ? cart.getUser().getUsername() : null;
        
        if (cart.getCartItems() != null) {
            this.items = cart.getCartItems().stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
            
            this.totalPrice = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            this.totalItems = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        } else {
            this.totalPrice = BigDecimal.ZERO;
            this.totalItems = 0;
        }
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}

class CartItemResponse {
    
    private Long itemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
    private String photo;

    public CartItemResponse() {}

    public CartItemResponse(CartItem item) {
        this.itemId = item.getId();
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.price = item.getProduct().getPrice();
        this.quantity = item.getQuantity();
        this.subtotal = item.getProduct().getPrice()
            .multiply(BigDecimal.valueOf(item.getQuantity()));
        this.photo = item.getProduct().getPhoto();
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}