package com.salesSavvy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.salesSavvy.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
	
    // Fetch a single user by username (basic query - no joins needed for login)
    Users findByUsername(String username);
    
    // Fetch user with cart loaded (use when cart is needed)
    @EntityGraph(attributePaths = {"cart", "cart.cartItems", "cart.cartItems.product"})
    Optional<Users> findWithCartByUsername(String username);
    
    // Fetch user with orders loaded (use when order history is needed)
    @EntityGraph(attributePaths = {"orders", "orders.items"})
    Optional<Users> findWithOrdersByUsername(String username);

    // Fetch all users by role
    List<Users> findByRole(String role);
    
    // Check if username exists (efficient count query)
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
}