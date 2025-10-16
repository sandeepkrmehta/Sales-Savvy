import React, { useEffect, useState } from "react";
import ProductCard from "../components/ProductCard";
import Customer_Orders from "../pages/Customer_Orders";
import { useNavigate } from "react-router-dom";
import { authService } from "../utils/auth";
import { api } from "../utils/api";

export default function Customer_home() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const [userId, setUserId] = useState(null);
  const [cartItems, setCartItems] = useState([]);

  const navigate = useNavigate();
  const username = authService.getUsername();

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await fetch("http://localhost:8080/getAllProducts");
        if (!res.ok) throw new Error("Failed to fetch products");
        setProducts(await res.json());
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    const fetchUser = async () => {
      if (!username) return;
      try {
        const res = await api.get(`/user/${username}`);
        if (!res.ok) throw new Error("Failed to fetch user info");
        const data = await res.json();
        setUserId(data.id);

        const cartRes = await api.get(`/getCart/${username}`);
        if (!cartRes.ok) throw new Error("Failed to fetch cart");
        setCartItems(await cartRes.json());
      } catch (err) {
        console.error(err);
      }
    };

    fetchProducts();
    fetchUser();
  }, [username]);

  async function handleAddToCart(product, qty = 1) {
    if (!username) return alert("Please login first!");

    try {
      const res = await api.post("/addToCart", {
        username,
        productId: product.id,
        quantity: qty,
      });

      if (res.ok) {
        alert(`Added "${product.name}" x${qty} to cart`);
        const cartRes = await api.get(`/getCart/${username}`);
        setCartItems(await cartRes.json());
      } else {
        const msg = await res.text();
        alert(`Failed: ${msg}`);
      }
    } catch (err) {
      console.error(err);
      alert("Error adding to cart");
    }
  }

  const handleLogout = () => {
    authService.logout();
    alert("Logged out successfully!");
    navigate("/");
  };

  const filtered = products.filter((p) =>
    (p.name + p.description).toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="customer-home">
      <nav className="navbar">
        <div className="logo">Sales Savvy</div>
        <input
          type="text"
          placeholder="Search products..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <div className="nav-icons">
          <button onClick={() => navigate("/cart")}>🛒 {cartItems.length}</button>
          <button onClick={handleLogout}>Logout</button>
          <button onClick={() => navigate(`/profile/${userId}`)}>👤 Profile</button>
        </div>
      </nav>

      <div className="products-section">
        {loading && <p>Loading...</p>}
        {error && <p className="error">{error}</p>}

        {!loading && !error && filtered.length > 0 ? (
          <div className="products-grid">
            {filtered.map((p) => (
              <ProductCard key={p.id} product={p} onAddToCart={handleAddToCart} />
            ))}
          </div>
        ) : (
          <p>No products found.</p>
        )}
      </div>

      {userId && <Customer_Orders userId={userId} />}
    </div>
  );
}
