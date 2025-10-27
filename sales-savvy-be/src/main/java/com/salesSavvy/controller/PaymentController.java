package com.salesSavvy.controller;

import com.salesSavvy.dto.PaymentRequest;
import com.salesSavvy.dto.PaymentVerifyRequest;
import com.salesSavvy.entity.Orders;
import com.salesSavvy.service.PaymentService;
import com.salesSavvy.service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody PaymentRequest paymentRequest) {
        try {
            Order razorpayOrder = paymentService.createRazorpayOrder(paymentRequest.getAmount());

            JSONObject orderJson = new JSONObject(razorpayOrder.toString());
            
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", orderJson.getString("id"));
            response.put("amount", orderJson.getInt("amount"));
            response.put("currency", orderJson.getString("currency"));
            response.put("key", paymentService.getKeyId());

            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create order");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestBody PaymentVerifyRequest verifyRequest,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();

        try {
//            System.out.println("=== STEP 1: Starting payment verification ===");
//            System.out.println("Received payment data:");
//            System.out.println(" - Order ID: " + verifyRequest.getOrderId());
//            System.out.println(" - Payment ID: " + verifyRequest.getPaymentId());
//            System.out.println(" - Signature: " + verifyRequest.getSignature());
//            System.out.println(" - Amount: " + verifyRequest.getAmount());
//            System.out.println(" - Principal: " + (principal != null ? principal.getName() : "NULL"));

            // ✅ 1. Verify Razorpay signature
            System.out.println("=== STEP 2: Verifying Razorpay signature ===");
            boolean isValid = paymentService.verifySignature(
                    verifyRequest.getOrderId(),
                    verifyRequest.getPaymentId(),
                    verifyRequest.getSignature()
            );

//            System.out.println("Signature verification result: " + isValid);

            if (!isValid) {
                System.out.println("❌ Signature verification FAILED");
                response.put("status", "error");
                response.put("message", "Payment verification failed");
                return ResponseEntity.badRequest().body(response);
            }

            System.out.println("✅ Signature verification PASSED");

            // ✅ 2. Get username
            System.out.println("=== STEP 3: Getting username ===");
            String username = "sandeep"; // Force username for testing
            System.out.println("Using username: " + username);

            // ✅ 3. Create Order from Cart
            System.out.println("=== STEP 4: Creating order from cart ===");
            System.out.println("Calling orderService.createOrderFromCart...");
            
            Orders createdOrder = orderService.createOrderFromCart(
                    username,
                    verifyRequest.getOrderId(),
                    verifyRequest.getAmount()
            );
            
            System.out.println("✅ Order created successfully with ID: " + createdOrder.getId());

            // ✅ 4. Update Order Payment Details
            System.out.println("=== STEP 5: Updating payment details ===");
            orderService.updatePaymentDetails(
                    verifyRequest.getOrderId(),
                    verifyRequest.getPaymentId(),
                    "PAID"
            );

//            System.out.println("✅ Payment details updated successfully");

            // ✅ 5. Send success response
//            System.out.println("=== STEP 6: Sending success response ===");
            response.put("status", "success");
            response.put("message", "Payment verified and order created successfully");
            response.put("orderId", createdOrder.getId());
            response.put("amount", createdOrder.getAmount());
            
//            System.out.println("🎉 PAYMENT VERIFICATION COMPLETED SUCCESSFULLY");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
//            System.out.println("❌ ERROR IN PAYMENT VERIFICATION:");
//            System.out.println("Error message: " + e.getMessage());
//            System.out.println("Error class: " + e.getClass().getName());
            e.printStackTrace(); // This will show the exact line where it fails
            
            response.put("status", "error");
            response.put("message", "Payment was successful but there was an issue creating your order");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/key")
    public ResponseEntity<Map<String, String>> getKey() {
        Map<String, String> response = new HashMap<>();
        response.put("key", paymentService.getKeyId());
        return ResponseEntity.ok(response);
    }
}