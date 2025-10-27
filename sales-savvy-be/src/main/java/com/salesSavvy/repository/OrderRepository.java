package com.salesSavvy.repository;

import com.salesSavvy.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(Long userId);
    List<Orders> findByUserUsername(String username);
    List<Orders> findByStatus(String status);
    Optional<Orders> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Orders> findByPaymentId(String paymentId);
    List<Orders> findAllByOrderByCreatedAtDesc();
    Long countByUserId(Long userId);
}