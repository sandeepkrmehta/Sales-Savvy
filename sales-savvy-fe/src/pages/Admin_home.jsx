import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { authService } from "../utils/auth";

export default function Admin_home() {
  const navigate = useNavigate();

  const handleLogout = () => {
    authService.logout();
    alert("Logged out successfully!");
    navigate("/");
  };

  return (
    <div className="admin-home-container">
      <div className="admin-header">
        <h2>👋 Welcome to Admin Dashboard</h2>
        <button className="logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </div>

      <div className="admin-cards-wrapper">
        <NavLink to="/pm" className="admin-card">
          <div className="card-icon">📦</div>
          <div className="card-title">Product Management</div>
        </NavLink>

        <NavLink to="/um" className="admin-card">
          <div className="card-icon">👤</div>
          <div className="card-title">User Management</div>
        </NavLink>

        <NavLink to="/admin-orders" className="admin-card">
          <div className="card-icon">🛒</div>
          <div className="card-title">Manage Orders</div>
        </NavLink>
      </div>
    </div>
  );
}
