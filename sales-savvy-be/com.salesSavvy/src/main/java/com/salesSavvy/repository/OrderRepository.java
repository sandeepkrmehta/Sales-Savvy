package com.salesSavvy.repository;

import com.salesSavvy.entity.Orders;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    
    // Using @EntityGraph instead of JOIN FETCH (more elegant solution)
    @EntityGraph(attributePaths = {"items", "items.product", "user"})
    List<Orders> findByUserId(Long userId);

    // Get orders by status
    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Orders> findByStatus(String status);

    // Find order by Razorpay order id with items loaded
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Orders> findByRazorpayOrderId(String razorpayOrderId);
    
    // Get all orders with items loaded (for admin)
    @EntityGraph(attributePaths = {"items", "items.product", "user"})
    List<Orders> findAll();
}