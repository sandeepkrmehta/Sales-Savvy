import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Add_product() {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);
  const [photo, setPhoto] = useState("");
  const [category, setCategory] = useState("");

  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();

    const data = { name, description, price, photo, category };

    try {
      const resp = await fetch("http://localhost:8080/addProduct", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      const msg = await resp.text();
      alert(msg);

      if (msg === "product added successfully!") {
        navigate("/pm");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Failed to submit data");
    }
  }

  return (
    <div className="container mt-4">
      {/* 🟢 Header */}
      <header className="mb-4 p-3 bg-primary text-white rounded">
        <h2 className="mb-0">Admin Panel</h2>
        <p className="mb-0">Add a new product to your store</p>
      </header>

      {/* 🟢 Form Card */}
      <div className="card p-4 shadow-sm">
        <h4 className="mb-3">Add Product</h4>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Name:</label>
            <input
              type="text"
              className="form-control"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Description:</label>
            <textarea
              className="form-control"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows="3"
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Price:</label>
            <input
              type="number"
              className="form-control"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Photo URL:</label>
            <input
              type="text"
              className="form-control"
              value={photo}
              onChange={(e) => setPhoto(e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Category:</label>
            <input
              type="text"
              className="form-control"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary w-100">
            Add Product
          </button>
        </form>
      </div>
    </div>
  );
}
