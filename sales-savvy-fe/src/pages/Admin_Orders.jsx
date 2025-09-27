import React, { useEffect, useState } from "react";
import OrdersTable from "../pages/OrdersTable";

export default function Admin_Orders() {
  const [orders, setOrders] = useState([]);

  const fetchOrders = async () => {
    const res = await fetch("http://localhost:8080/api/orders/all");
    const data = await res.json();
    setOrders(data);
  };

  const updateStatus = async (razorpayOrderId, status) => {
    const res = await fetch(
      `http://localhost:8080/api/orders/admin/update-status/${razorpayOrderId}?status=${status}`,
      { method: "PUT" }
    );
    const msg = await res.text();
    alert(msg);
    fetchOrders();
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  return (
    <div>
      <h2>All Orders (Admin)</h2>
      <OrdersTable
        orders={orders}
        actions={(o) =>
          o.status !== "PAID" && (
            <>
              <button onClick={() => updateStatus(o.id, "PAID")}>Mark PAID</button>{" "}
              <button onClick={() => updateStatus(o.id, "CANCELLED")}>Cancel</button>
            </>
          )
        }
      />
    </div>
  );
}
