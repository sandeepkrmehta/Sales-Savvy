package com.salesSavvy.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment PK
    private Long id;

    @Column(name = "razorpay_order_id", unique = true, nullable = false)
    private String razorpayOrderId;   // Razorpay se milne wala orderId

    private double amount;       // in paise
    private String currency;     // "INR"

    @NotBlank
    private String status;       // CREATED | PAID

    private String receipt;      // optional
    private String paymentId;    // Razorpay payment id (after success)

    @ManyToOne
    @JsonIgnoreProperties({"orders"}) // ignore the back-reference in Users
    private Users user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> items;   // snapshot of cart at payment time

    public Orders() {}

    /* ✅ Getters and Setters */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReceipt() { return receipt; }
    public void setReceipt(String receipt) { this.receipt = receipt; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    @Override
    public String toString() {
        return "Orders [id=" + id + ", razorpayOrderId=" + razorpayOrderId +
               ", amount=" + amount + ", currency=" + currency +
               ", status=" + status + ", paymentId=" + paymentId + ", user=" + user + "]";
    }
}
