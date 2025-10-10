package com.salesSavvy.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesSavvy.entity.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // Find cart by user ID with items loaded
    @EntityGraph(attributePaths = {"cartItems", "cartItems.product"})
    Optional<Cart> findByUserId(Long userId);
    
    // Find cart by username (through user relationship)
    @EntityGraph(attributePaths = {"cartItems", "cartItems.product", "user"})
    @Query("SELECT c FROM Cart c WHERE c.user.username = :username")
    Optional<Cart> findByUsername(@Param("username") String username);
    
    // Check if cart exists for user
    boolean existsByUserId(Long userId);
    
    // Count items in cart
    @Query("SELECT COUNT(ci) FROM Cart c JOIN c.cartItems ci WHERE c.user.id = :userId")
    Long countItemsByUserId(@Param("userId") Long userId);
    
    // Get total cart value for user
    @Query("SELECT SUM(ci.product.price * ci.quantity) FROM Cart c " +
           "JOIN c.cartItems ci WHERE c.user.id = :userId")
    Double getTotalValueByUserId(@Param("userId") Long userId);
}