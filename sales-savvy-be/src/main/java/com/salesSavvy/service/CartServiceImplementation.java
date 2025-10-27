package com.salesSavvy.service;

import com.salesSavvy.entity.Cart;
import com.salesSavvy.entity.CartItem;
import com.salesSavvy.entity.Product;
import com.salesSavvy.entity.Users;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.repository.CartRepository;
import com.salesSavvy.repository.CartItemRepository;
import com.salesSavvy.repository.ProductRepository;
import com.salesSavvy.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;

    public CartServiceImplementation(CartRepository cartRepository,
                                   CartItemRepository cartItemRepository,
                                   UsersRepository usersRepository,
                                   ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void addToCart(String username, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Users user = usersRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseGet(() -> {
                Cart newCart = new Cart(user);
                return cartRepository.save(newCart);
            });

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // Add new item
            CartItem newItem = new CartItem(cart, product, quantity);
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }
    }

    @Override
    @Transactional
    public void updateCartItem(String username, Long productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Users user = usersRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (quantity == 0) {
            // Remove item
            existingItem.ifPresent(cartItem -> {
                cart.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            });
        } else if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        } else {
            // Add new item with specified quantity
            CartItem newItem = new CartItem(cart, product, quantity);
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }
    }

    @Override
    @Transactional
    public void removeFromCart(String username, Long productId) {
        updateCartItem(username, productId, 0);
    }

    @Override
    @Transactional
    public void clearCart(String username) {
        Users user = usersRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));

        cartItemRepository.deleteByCart(cart);
        cart.getCartItems().clear();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(String username) {
        Cart cart = cartRepository.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));

        return cartItemRepository.findByCart(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCartByUsername(String username) {
        return cartRepository.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCartItemCount(String username) {
        Users user = usersRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));

        return cartItemRepository.countByCart(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCartTotalValue(String username) {
        List<CartItem> cartItems = getCartItems(username);
        return cartItems.stream()
            .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}