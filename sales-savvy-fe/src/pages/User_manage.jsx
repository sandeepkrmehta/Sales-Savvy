import React, { useState, useEffect } from "react";

export default function User_manage() {
  // 🟢 State to store users and search text
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");

  // 🟢 Fetch users on page load
  useEffect(() => {
    fetchUsers();
  }, []);

  async function fetchUsers() {
    try {
      const resp = await fetch("http://localhost:8080/user");
      const data = await resp.json();
      setUsers(data);
    } catch (err) {
      console.error("Error fetching users:", err);
      alert("Failed to load users");
    }
  }

  // 🟢 Delete user by ID
  async function handleDelete(id) {
    if (!window.confirm("Are you sure you want to delete this user?")) return;
    try {
      const resp = await fetch(`http://localhost:8080/deleteUser/${id}`, {
        method: "DELETE",
      });
      if (resp.ok) {
        alert("User deleted successfully!");
        fetchUsers(); // refresh list
      } else {
        alert("Failed to delete user");
      }
    } catch (err) {
      console.error("Error deleting user:", err);
    }
  }

  // 🟢 Optional: Update role
  async function handleRoleChange(id, newRole) {
    try {
      const resp = await fetch(`http://localhost:8080/user/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ role: newRole }),
      });
      if (resp.ok) {
        alert("User role updated!");
        fetchUsers();
      } else {
        alert("Failed to update role");
      }
    } catch (err) {
      console.error("Error updating role:", err);
    }
  }

  // 🟢 Filter users by search keyword
  const filteredUsers = users.filter((u) =>
    u.username.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="container">
      <h2>👤 User Management</h2>

      {/* ✅ Search bar */}
      <input
        type="text"
        placeholder="Search user by username..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="search-bar"
      />

      {/* ✅ User Table */}
      <table className="table table-bordered table-striped mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>DOB</th>
            <th>Gender</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredUsers.length > 0 ? (
            filteredUsers.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>{user.dob}</td>
                <td>{user.gender}</td>
                <td>
                  <select
                    value={user.role}
                    onChange={(e) => handleRoleChange(user.id, e.target.value)}
                  >
                    <option value="CUSTOMER">CUSTOMER</option>
                    <option value="ADMIN">ADMIN</option>
                  </select>
                </td>
                <td>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => handleDelete(user.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="7" className="text-center">
                No users found
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
