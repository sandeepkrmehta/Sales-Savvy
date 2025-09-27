import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function Update_product() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState("");

  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);
  const [photo, setPhoto] = useState("");
  const [category, setCategory] = useState("");

  const navigate = useNavigate();

  // 🟢 Fetch all products
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

  // 🟢 Populate form when Edit clicked
  function handleEdit(product) {
    setId(product.id);
    setName(product.name);
    setDescription(product.description);
    setPrice(product.price);
    setPhoto(product.photo);
    setCategory(product.category);
  }

  // 🟢 Update product
  async function handleUpdate(e) {
    e.preventDefault();
    const data = { name, description, price, photo, category };

    try {
      const res = await fetch(`http://localhost:8080/updateProduct/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      });

      const msg = await res.text();
      alert(msg);

      if (res.ok) {
        // Update the product in local state
        setProducts(products.map(p => p.id === id ? { ...p, ...data } : p));
        navigate("/pm");
      }
    } catch (err) {
      console.error(err);
      alert("Failed to update product");
    }
  }

  return (
    <div className="container mt-4">
      <h3>Update Products</h3>

      {loading && <p>Loading products…</p>}
      {error && <p className="text-danger">{error}</p>}

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
              <th>Edit</th>
            </tr>
          </thead>
          <tbody>
            {products.map(p => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>
                  <img
                    src={p.photo || "/placeholder.png"}
                    alt={p.name}
                    width="60"
                    onError={e => (e.target.src = "/placeholder.png")}
                  />
                </td>
                <td>{p.name}</td>
                <td>{p.description}</td>
                <td>{p.price}</td>
                <td>{p.category}</td>
                <td>
                  <button
                    className="btn btn-warning btn-sm"
                    onClick={() => handleEdit(p)}
                  >
                    Edit
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <h4 className="mt-4">Edit Product</h4>
      <form onSubmit={handleUpdate}>
        <div className="form-group">
          <label>Product ID:</label>
          <input type="text" value={id} readOnly className="form-control"/>
        </div>

        <div className="form-group">
          <label>Name:</label>
          <input type="text" value={name} onChange={e => setName(e.target.value)} className="form-control"/>
        </div>

        <div className="form-group">
          <label>Description:</label>
          <input type="text" value={description} onChange={e => setDescription(e.target.value)} className="form-control"/>
        </div>

        <div className="form-group">
          <label>Price:</label>
          <input type="number" value={price} onChange={e => setPrice(e.target.value)} className="form-control"/>
        </div>

        <div className="form-group">
          <label>Photo URL:</label>
          <input type="text" value={photo} onChange={e => setPhoto(e.target.value)} className="form-control"/>
        </div>

        <div className="form-group">
          <label>Category:</label>
          <input type="text" value={category} onChange={e => setCategory(e.target.value)} className="form-control"/>
        </div>

        <button type="submit" className="btn btn-warning mt-2">Update Product</button>
      </form>
    </div>
  );
}
