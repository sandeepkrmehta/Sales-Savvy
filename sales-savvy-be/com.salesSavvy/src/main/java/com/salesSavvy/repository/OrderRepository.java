package com.salesSavvy.repository;

import com.salesSavvy.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    
    // Get all orders of a specific customer
    List<Orders> findByUserId(Long userId);

    // Get orders by status (optional, useful for admin)
    List<Orders> findByStatus(String status);

    // Find order by Razorpay order id
    Optional<Orders> findByRazorpayOrderId(String razorpayOrderId);
}
