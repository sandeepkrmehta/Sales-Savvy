import React from "react";
import { NavLink } from "react-router-dom";

export default function Product_manage() {
  return (
    <div className="admin-home-container">
      <h2>🛠️ Product Management</h2>

      <div className="admin-cards-wrapper">
        <NavLink to="/addProd" className="admin-card">
          <div className="card-icon">➕</div>
          <div className="card-title">Add New Product</div>
        </NavLink>

        <NavLink to="/updateProd" className="admin-card">
          <div className="card-icon">✏️</div>
          <div className="card-title">Update Existing Product</div>
        </NavLink>

        <NavLink to="/deleteProd" className="admin-card">
          <div className="card-icon">🗑️</div>
          <div className="card-title">Delete Product</div>
        </NavLink>

        <NavLink to="/searchProd" className="admin-card">
          <div className="card-icon">🔍</div>
          <div className="card-title">All Product</div>
        </NavLink>
      </div>
    </div>
  );
}
