package com.salesSavvy.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.salesSavvy.entity.Cart;
import com.salesSavvy.entity.CartItem;
import com.salesSavvy.entity.Users;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.repository.CartRepository;
import com.salesSavvy.repository.UsersRepository;

@Service
public class CartServiceImplementation implements CartService {

    private final CartRepository  cartRepo;
    private final UsersRepository userRepo;

    public CartServiceImplementation(CartRepository cartRepo,
                                     UsersRepository userRepo) {
        this.cartRepo  = cartRepo;
        this.userRepo  = userRepo;
    }

    @Override
    public void addCart(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    public void clearCart(String username) {
        Cart cart = cartRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));
        
        cart.getCartItems().clear();
        cartRepo.save(cart);
    }

    @Override
    public List<CartItem> getItems(String username) {
        // Use optimized query with @EntityGraph
        Cart cart = cartRepo.findByUsername(username).orElse(null);
        
        if (cart == null || cart.getCartItems() == null) {
            return Collections.emptyList();
        }
        
        return cart.getCartItems();
    }

    @Override
    public List<CartItem> cloneItems(String username) {
        List<CartItem> items = getItems(username);
        
        return items.stream().map(src -> {
            CartItem copy = new CartItem();
            copy.setProduct(src.getProduct());
            copy.setQuantity(src.getQuantity());
            copy.setCart(null);  // Detach from original cart
            return copy;
        }).toList();
    }
    
    // Additional optimized methods
    
    public Cart getCartByUsername(String username) {
        return cartRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));
    }
    
    public Cart getCartByUserId(Long userId) {
        return cartRepo.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));
    }
    
    public Long getCartItemCount(Long userId) {
        return cartRepo.countItemsByUserId(userId);
    }
    
    public Double getCartTotalValue(Long userId) {
        Double total = cartRepo.getTotalValueByUserId(userId);
        return total != null ? total : 0.0;
    }
    
    public boolean cartExists(Long userId) {
        return cartRepo.existsByUserId(userId);
    }
}