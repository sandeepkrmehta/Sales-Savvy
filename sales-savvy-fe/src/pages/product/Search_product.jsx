import React, { useEffect, useState } from "react";

export default function Show_all_products() {
  // 🟢 State variables
  const [products, setProducts] = useState([]);   // store all products
  const [search, setSearch] = useState("");       // search keyword

  // 🟢 Fetch products when component loads
  useEffect(() => {
    fetchProducts();
  }, []);

  // 🟢 Function to fetch all products
  async function fetchProducts() {
    try {
      const resp = await fetch("http://localhost:8080/getAllProducts");
      const data = await resp.json();
      setProducts(data);
    } catch (error) {
      console.error("Error fetching products:", error);
      alert("Failed to fetch products");
    }
  }

  // 🟢 Filter products by search keyword (name)
  const filteredProducts = products.filter((p) =>
    p.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="container">
      <h3>🛍️ All Products</h3>

      {/* ✅ Search Bar */}
      <input
        type="text"
        placeholder="Search by product name..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="form-control mb-3"
      />

      {/* ✅ Product Table */}
      <table className="table table-bordered table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Photo</th>
            <th>Name</th>
            <th>Description</th>
            <th>Price (₹)</th>
            <th>Category</th>
          </tr>
        </thead>
        <tbody>
          {filteredProducts.length > 0 ? (
            filteredProducts.map((p) => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>
                  <img
                    src={p.photo || "/placeholder.png"}
                    alt={p.name}
                    width="80"
                    height="80"
                    onError={(e) => (e.target.src = "/placeholder.png")}
                  />
                </td>
                <td>{p.name}</td>
                <td>{p.description}</td>
                <td>{p.price}</td>
                <td>{p.category}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center">
                No products found
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
