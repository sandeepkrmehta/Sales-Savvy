import React, { useState } from "react";

export default function ProductCard({ product, onAddToCart }) {
  const [qty, setQty] = useState(1);

  return (
    <div className="product-card">
      <img src={product.photo} alt={product.name} />
      <h3>{product.name}</h3>
      <p>{product.description}</p>
      <p>₹{product.price}</p>

      <div className="card-actions">
        <input
          type="number"
          min="1"
          value={qty}
          onChange={(e) => setQty(Number(e.target.value))}
        />
        <button onClick={() => onAddToCart(product, qty)}>Add to Cart</button>
      </div>
    </div>
  );
}
