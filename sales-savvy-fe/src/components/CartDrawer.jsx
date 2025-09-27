import React, { useEffect, useState } from "react";
import loadRazorpay from "../utils/loadRzp";
// import "./CartDrawer.css";

export default function CartDrawer({ isOpen, onClose }) {
  const [items, setItems] = useState([]);
  const username = localStorage.getItem("username");

  useEffect(() => {
    if (isOpen && username) {
      fetchCart();
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen, username]);

  const fetchCart = async () => {
    const res = await fetch(`http://localhost:8080/getCart/${username}`);
    if (res.ok) setItems(await res.json());
  };

  const total = items.reduce((s, it) => s + it.product.price * it.quantity, 0);

  // Update quantity
  const updateQty = async (productId, qty) => {
    if (qty < 1) return;
    const res = await fetch("http://localhost:8080/updateCart", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, productId, quantity: qty }),
    });
    if (res.ok) fetchCart();
  };

  // Remove item
  const removeItem = async (productId) => {
    const res = await fetch(`http://localhost:8080/removeFromCart/${username}/${productId}`, {
      method: "DELETE",
    });
    if (res.ok) fetchCart();
  };

  const payNow = async () => {
    if (!items.length) return;
    const ok = await loadRazorpay();
    if (!ok) return alert("Razorpay SDK failed to load");

    const res = await fetch("http://localhost:8080/payment/create", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, amount: total * 100 }),
    });
    const data = await res.json();

    const rzp = new window.Razorpay({
      key: data.key,
      amount: data.amount,
      currency: "INR",
      name: "Sales Savvy",
      description: "Order Payment",
      order_id: data.orderId,
      handler: async (resp) => {
        const vr = await fetch("http://localhost:8080/payment/verify", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username,
            orderId: resp.razorpay_order_id,
            paymentId: resp.razorpay_payment_id,
            signature: resp.razorpay_signature,
          }),
        });
        const orderId = await vr.text();
        alert(`Payment successful! Order ID: ${orderId}`);
        onClose();
      },
    });
    rzp.open();
  };

  return (
    <div className={`cart-drawer ${isOpen ? "open" : ""}`}>
      <div className="cart-header">
        <h3>🛒 My Cart</h3>
        <button className="close-btn" onClick={onClose}>
          &times;
        </button>
      </div>

      <div className="cart-items">
        {items.length === 0 && <p>Your cart is empty.</p>}
        {items.map((it) => (
          <div className="cart-item" key={it.product.id}>
            <img src={it.product.image} alt={it.product.name} />
            <div className="item-info">
              <h4>{it.product.name}</h4>
              <p>₹{it.product.price}</p>
              <div className="qty-controls">
                <button onClick={() => updateQty(it.product.id, it.quantity - 1)}>-</button>
                <span>{it.quantity}</span>
                <button onClick={() => updateQty(it.product.id, it.quantity + 1)}>+</button>
              </div>
              <button className="remove-btn" onClick={() => removeItem(it.product.id)}>
                Remove
              </button>
            </div>
            <p className="subtotal">₹{it.product.price * it.quantity}</p>
          </div>
        ))}
      </div>

      {items.length > 0 && (
        <div className="cart-footer">
          <h4>Total: ₹{total}</h4>
          <button className="pay-btn" onClick={payNow}>
            Pay Now
          </button>
        </div>
      )}
    </div>
  );
}
