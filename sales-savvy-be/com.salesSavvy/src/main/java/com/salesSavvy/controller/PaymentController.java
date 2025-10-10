package com.salesSavvy.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.salesSavvy.dto.*;
import com.salesSavvy.entity.*;
import com.salesSavvy.exception.BadRequestException;
import com.salesSavvy.exception.ResourceNotFoundException;
import com.salesSavvy.repository.OrderRepository;
import com.salesSavvy.service.*;

/**
 * Handles payment creation, verification, and order summary retrieval.
 */
@RestController
@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired private PaymentService  payService;
    @Autowired private UsersService    usersService;
    @Autowired private CartService     cartService;
    @Autowired private OrderRepository orderRepo;

    /* ---------- 1️⃣ CREATE Razorpay Order ---------- */
    @PostMapping("/payment/create")
    @Transactional
    public ResponseEntity<?> create(@RequestBody PaymentRequest req) {
        try {
            Users u = usersService.getUser(req.username);
            if (u == null) {
                throw new ResourceNotFoundException("User", "username", req.username);
            }

            if (req.amount <= 0) {
                throw new BadRequestException("Amount must be greater than 0");
            }

            Order rzp = payService.createRzpOrder(req.amount);

            Orders o = new Orders();
            o.setRazorpayOrderId(String.valueOf(rzp.get("id")));
            o.setAmount(BigDecimal.valueOf(req.amount));
            o.setCurrency("INR");
            o.setStatus("CREATED");
            o.setUser(u);
            o.setItems(cartService.cloneItems(req.username));

            orderRepo.save(o);

            // ✅ Fixed ambiguous logger
            logger.info("Order created successfully: {}", String.valueOf(rzp.get("id")));

            return ResponseEntity.ok(Map.of(
                "key",     payService.getKeyId(),
                "orderId", rzp.get("id"),
                "amount",  req.amount
            ));

        } catch (RazorpayException e) {
            logger.error("Razorpay order creation failed: {}", String.valueOf(e.getMessage()));
            throw new BadRequestException("Failed to create payment order: " + e.getMessage());
        }
    }

    /* ---------- 2️⃣ VERIFY Razorpay Payment ---------- */
    @PostMapping("/payment/verify")
    @Transactional
    public ResponseEntity<?> verify(@RequestBody PaymentVerifyRequest req) {

        try {
            if (!payService.verifySignature(req.orderId, req.paymentId, req.signature)) {
                logger.warn("Payment signature verification failed for order: {}", String.valueOf(req.orderId));
                throw new BadRequestException("Payment signature verification failed");
            }

            Orders ord = orderRepo.findByRazorpayOrderId(req.orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "razorpayOrderId", req.orderId));

            ord.setStatus("PAID");
            ord.setPaymentId(req.paymentId);
            orderRepo.saveAndFlush(ord);

            cartService.clearCart(req.username);

            logger.info("Payment verified successfully: {}", String.valueOf(req.orderId));

            return ResponseEntity.ok(Map.of(
                "message", "Payment successful",
                "orderId", ord.getRazorpayOrderId()
            ));

        } catch (org.hibernate.StaleObjectStateException | 
                 jakarta.persistence.OptimisticLockException ex) {
            logger.error("Optimistic locking conflict for order: {}", String.valueOf(req.orderId));
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Order was updated by another process. Please try again."));
        }
    }

    /* ---------- 3️⃣ FETCH Order Details ---------- */
    @GetMapping("/order/{razorpayOrderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String razorpayOrderId) {

        Orders order = orderRepo.findByRazorpayOrderId(razorpayOrderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "razorpayOrderId", razorpayOrderId));

        logger.info("Fetched order details for ID: {}", String.valueOf(razorpayOrderId));

        return ResponseEntity.ok(new OrderResponse(order));
    }

    /* ---------- 4️⃣ FETCH Order Summary ---------- */
    @GetMapping("/order/summary/{razorpayOrderId}")
    public ResponseEntity<Map<String, Object>> getOrderSummary(@PathVariable String razorpayOrderId) {

        Orders o = orderRepo.findByRazorpayOrderId(razorpayOrderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "razorpayOrderId", razorpayOrderId));

        BigDecimal totalRupees = o.getAmount().divide(BigDecimal.valueOf(100));

        Map<String, Object> summary = Map.of(
            "orderId", o.getRazorpayOrderId(),
            "status",  o.getStatus(),
            "total",   totalRupees,
            "items",   o.getItems().stream().map(ci -> Map.of(
                           "name",  ci.getProduct().getName(),
                           "qty",   ci.getQuantity(),
                           "price", ci.getProduct().getPrice()
                        )).toList()
        );

        logger.info("Generated order summary for: {}", String.valueOf(razorpayOrderId));

        return ResponseEntity.ok(summary);
    }
}
