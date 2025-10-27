package com.salesSavvy.controller;

import com.salesSavvy.dto.CartResponse;
import com.salesSavvy.dto.CartItemResponse;
import com.salesSavvy.entity.Cart;
import com.salesSavvy.entity.CartItem;
import com.salesSavvy.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Principal principal) {
    	
    	 String username = principal.getName();
        cartService.addToCart(username, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(
            @RequestParam String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        cartService.updateCartItem(username, productId, quantity);
        return ResponseEntity.ok("Cart updated successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(
            @RequestParam String username,
            @RequestParam Long productId) {
        cartService.removeFromCart(username, productId);
        return ResponseEntity.ok("Product removed from cart successfully");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam String username) {
        cartService.clearCart(username);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    @GetMapping("/items")
    public ResponseEntity<CartResponse> getCartItems(@RequestParam String username) {
        Cart cart = cartService.getCartByUsername(username);
        List<CartItem> cartItems = cartService.getCartItems(username);

        List<CartItemResponse> itemResponses = cartItems.stream()
            .map(item -> new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getProduct().getPhoto()
            ))
            .collect(Collectors.toList());

        CartResponse response = new CartResponse(
            cart.getId(),
            username,
            itemResponses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {
        Integer count = cartService.getCartItemCount(username);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotalValue(@RequestParam String username) {
        Double total = cartService.getCartTotalValue(username).doubleValue();
        return ResponseEntity.ok(total);
    }
}