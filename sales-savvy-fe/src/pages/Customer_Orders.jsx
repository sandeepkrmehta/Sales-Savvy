import React, { useEffect, useState } from "react";

export default function Customer_Orders({ userId }) {
const [orders, setOrders] = useState([]);

  const fetchOrders = async () => {
    if (!userId) return;
    const res = await fetch(`http://localhost:8080/api/orders/user/${userId}`);
    if (!res.ok) return alert("Failed to fetch orders");
    const data = await res.json();
    setOrders(data);
  };

  const cancelOrder = async (razorpayOrderId) => {
    const res = await fetch(
      `http://localhost:8080/api/orders/user/cancel/${razorpayOrderId}`,
      { method: "PUT" }
    );
    const msg = await res.text();
    alert(msg);
    fetchOrders();
  };

  useEffect(() => {
    fetchOrders();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  return (
    <div>
      <h2>My Orders</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Amount (INR)</th>
            <th>Status</th>
            <th>Items</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((o) => (
            <tr key={o.razorpayOrderId}>
              <td>{o.razorpayOrderId}</td>
              <td>{o.amount.toFixed(2)}</td>
              <td>{o.status}</td>
              <td>{o.items.join(", ")}</td>
              <td>
                {o.status !== "PAID" && (
                  <button onClick={() => cancelOrder(o.razorpayOrderId)}>
                    Cancel
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
