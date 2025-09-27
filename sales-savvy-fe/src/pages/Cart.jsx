import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import loadRazorpay from "../utils/loadRzp";
// import "./Cart.css";

export default function Cart() {
  const [items, setItems] = useState([]);
  const [loading, setLoad] = useState(true);
  const [error, setErr] = useState("");

  const username = localStorage.getItem("username");
  const navigate = useNavigate();

  useEffect(() => {
    if (!username) return;
    (async () => {
      try {
        const r = await fetch(`http://localhost:8080/getCart/${username}`);
        if (!r.ok) throw new Error("Failed to fetch cart");
        setItems(await r.json());
      } catch (e) {
        setErr(e.message);
      } finally {
        setLoad(false);
      }
    })();
  }, [username]);

  const total = items.reduce((s, it) => s + it.product.price * it.quantity, 0);

  async function payNow() {
    if (!items.length) return;

    const ok = await loadRazorpay();
    if (!ok) return alert("Razorpay SDK failed to load. Check your internet.");

    const res = await fetch("http://localhost:8080/payment/create", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, amount: total * 100 }),
    });
    if (!res.ok) return alert(await res.text());
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
        if (!vr.ok) return alert(await vr.text());
        const orderId = await vr.text();
        navigate(`/order-summary/${orderId}`);
      },
      prefill: {
        name: username,
        email: localStorage.getItem("email") || "",
      },
      theme: { color: "#3399cc" },
    });
    rzp.open();
  }

  return (
    <div className="container cart-view">
      <h2 className="text-center">🛒 Your Cart</h2>

      {loading && <p className="text-center">Loading…</p>}
      {error && <p className="text-center text-danger">{error}</p>}

      {!loading && !error && items.length === 0 && (
        <p className="text-center empty-cart">Your cart is empty.</p>
      )}

      {!loading && !error && items.length > 0 && (
        <div className="cart-content">
          <table className="cart-table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              {items.map((it) => (
                <tr key={it.product.id}>
                  <td className="product-cell">
                    <img src={it.product.image} alt={it.product.name} />
                    <span>{it.product.name}</span>
                  </td>
                  <td>₹{it.product.price}</td>
                  <td>{it.quantity}</td>
                  <td>₹{it.product.price * it.quantity}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="cart-summary">
            <h3>Total: ₹{total}</h3>
            <button className="btn btn-primary pay-btn" onClick={payNow}>
              Pay with Razorpay
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
