import React from "react";

export default function OrdersTable({ orders, actions }) {
  return (
    <table border="1" cellPadding="8" cellSpacing="0" style={{ width: "100%" }}>
      <thead>
        <tr>
          <th>Razorpay Order ID</th>
          <th>User ID</th>
          <th>Amount</th>
          <th>Status</th>
          {actions && <th>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {orders.length === 0 ? (
          <tr>
            <td colSpan={actions ? 5 : 4} style={{ textAlign: "center" }}>
              No orders found
            </td>
          </tr>
        ) : (
          orders.map((o) => (
            <tr key={o.id}>
              <td>{o.id}</td>
              <td>{o.user?.id || "-"}</td>
              <td>{(o.amount / 100).toFixed(2)} INR</td>
              <td>{o.status}</td>
              {actions && <td>{actions(o)}</td>}
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}
