import React, { useState, useEffect } from "react";
// import { useNavigate } from "react-router-dom";

export default function Delete_product() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState("");
  // const navigate = useNavigate();

  // 🟢 Fetch all products on load
  useEffect(() => {
    fetchProducts();
  }, []);

  async function fetchProducts() {
    try {
      const res = await fetch("http://localhost:8080/getAllProducts");
      if (!res.ok) throw new Error("Failed to fetch products");
      setProducts(await res.json());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  // 🟢 Delete product by id
  async function handleDelete(id) {
    if (!window.confirm("Are you sure you want to delete this product?")) return;

    try {
      const res = await fetch(`http://localhost:8080/deleteProduct/${id}`, {
        method: "DELETE"
      });
      const msg = await res.text();
      alert(msg);

      if (res.ok) {
        // Remove deleted product from UI
        setProducts(products.filter((p) => p.id !== id));
      }
    } catch (err) {
      console.error(err);
      alert("Failed to delete product");
    }
  }

  return (
    <div className="container mt-4">
      <h3>Delete Products</h3>

      {loading && <p>Loading products…</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && products.length === 0 && (
        <p>No products available</p>
      )}

      {!loading && !error && products.length > 0 && (
        <table className="table table-bordered table-striped mt-3">
          <thead>
            <tr>
              <th>ID</th>
              <th>Photo</th>
              <th>Name</th>
              <th>Description</th>
              <th>Price</th>
              <th>Category</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {products.map((p) => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>
                  <img
                    src={p.photo || "/placeholder.png"}
                    alt={p.name}
                    width="60"
                    onError={(e) => (e.target.src = "/placeholder.png")}
                  />
                </td>
                <td>{p.name}</td>
                <td>{p.description}</td>
                <td>{p.price}</td>
                <td>{p.category}</td>
                <td>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => handleDelete(p.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
