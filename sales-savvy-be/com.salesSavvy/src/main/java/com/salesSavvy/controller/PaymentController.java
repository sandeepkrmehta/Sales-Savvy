package com.salesSavvy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.salesSavvy.dto.*;
import com.salesSavvy.entity.*;
import com.salesSavvy.repository.OrderRepository;
import com.salesSavvy.service.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class PaymentController {

    @Autowired PaymentService  payService;
    @Autowired UsersService    usersService;
    @Autowired CartService     cartService;
    @Autowired OrderRepository orderRepo;

    /* ---------- 1. create Razorpay order ---------- */
    @PostMapping("/payment/create")
    public ResponseEntity<?> create(@RequestBody PaymentRequest req) throws RazorpayException {

        Users u = usersService.getUser(req.username);
        if (u == null) return ResponseEntity.badRequest().body("user not found");

        Order rzp = payService.createRzpOrder(req.amount);

        Orders o  = new Orders();
        o.setRazorpayOrderId(rzp.get("id"));   // ✅ store Razorpay ID separately
        o.setAmount(req.amount);
        o.setCurrency("INR");
        o.setStatus("CREATED");
        o.setUser(u);
        o.setItems(cartService.cloneItems(req.username));
        orderRepo.save(o);

        return ResponseEntity.ok(Map.of(
            "key",     payService.getKeyId(),
            "orderId", rzp.get("id"),
            "amount",  req.amount
        ));
    }

    /* ---------- 2. verify after payment ---------- */
    @PostMapping("/payment/verify")
    public ResponseEntity<?> verify(@RequestBody PaymentVerifyRequest req) throws RazorpayException {

        if (!payService.verifySignature(req.orderId, req.paymentId, req.signature))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("signature mismatch");

        try {
            Orders ord = orderRepo.findByRazorpayOrderId(req.orderId).orElseThrow(); // ✅ lookup by Razorpay ID
            ord.setStatus("PAID");
            ord.setPaymentId(req.paymentId);
            orderRepo.saveAndFlush(ord);

            cartService.clearCart(req.username);   // empty cart

            return ResponseEntity.ok(ord.getRazorpayOrderId()); // return Razorpay ID
        } catch (org.hibernate.StaleObjectStateException | jakarta.persistence.OptimisticLockException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Order was updated by another process. Please try again.");
        }
    }

    /* ---------- 3a. raw entity (optional) ---------- */
    @GetMapping("/order/{razorpayOrderId}")
    public Orders getOrder(@PathVariable String razorpayOrderId) {
        return orderRepo.findByRazorpayOrderId(razorpayOrderId).orElse(null);
    }

    /* ---------- 3b. lean summary for React ---------- */
    @GetMapping("/order/summary/{razorpayOrderId}")
    public Map<String, Object> getOrderSummary(@PathVariable String razorpayOrderId) {

        Orders o = orderRepo.findByRazorpayOrderId(razorpayOrderId).orElse(null);
        if (o == null) return Map.of("error", "not-found");

        double totalRupees = o.getAmount() / 100;

        return Map.of(
            "orderId", o.getRazorpayOrderId(),  // ✅ return Razorpay ID
            "status",  o.getStatus(),
            "total",   totalRupees,
            "items",   o.getItems().stream().map(ci -> Map.of(
                           "name",  ci.getProduct().getName(),
                           "qty",   ci.getQuantity(),
                           "price", ci.getProduct().getPrice()
                        )).toList()
        );
    }
}
