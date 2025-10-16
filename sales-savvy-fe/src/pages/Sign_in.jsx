import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { authService } from "../utils/auth";

export default function Sign_in() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);

    const data = { username, password };

    try {
      const resp = await fetch("http://localhost:8080/signIn", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });

      if (!resp.ok) {
        const errorData = await resp.json();
        alert(errorData.message || "Login failed");
        return;
      }

      const result = await resp.json();

      authService.setAuth(result.token, result.username, result.role);

      const role = result.role.toLowerCase();
      navigate(`/${role}_home`);
    } catch (err) {
      console.error("Login error:", err);
      alert("Could not sign in");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-wrap center-block card mt-6">
      <h4 className="text-center mb-4">Sign in</h4>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Username</label>
          <input
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <button className="btn btn-primary w-100" type="submit" disabled={loading}>
          {loading ? "Logging in..." : "Log in"}
        </button>
      </form>
    </div>
  );
}
