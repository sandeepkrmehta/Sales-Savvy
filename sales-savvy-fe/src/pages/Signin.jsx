import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function Signin() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const validate = () => {
    const errs = {};
    if (!username.trim()) errs.username = "Username is required";
    if (!password.trim()) errs.password = "Password is required";
    else if (password.length < 5) errs.password = "Password must be at least 5 characters";
    return errs;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length) {
      setErrors(errs);
      return;
    }
    setErrors({});

    axios
      .post('http://localhost:8080/signIn', { username, password })
      .then(res => {
        localStorage.setItem('username', username);
        const role = res.data;
        if (role === 'admin') navigate('/admin_page');
        else if (role === 'customer') navigate('/customer_page');
        else alert('Unknown role: ' + role);
      })
      .catch(err => {
        console.error(err);
        alert('Error signing in – check console');
      });
  };

  return (
    <div className="container">
      <form className="form-container" onSubmit={handleSubmit}>
        <h2>Sign In</h2>

        <div className="form-group">
          <label>Username:<sup style={{ color: 'red' }}>*</sup></label>
          <input
            type="text"
            value={username}
            onChange={e => setUsername(e.target.value)}
            required
          />
          {errors.username && <p className="error">{errors.username}</p>}
        </div>

        <div className="form-group">
          <label>Password:<sup style={{ color: 'red' }}>*</sup></label>
          <input
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
          {errors.password && <p className="error">{errors.password}</p>}
        </div>

        <button className="btn btn-primary" type="submit">Sign In</button>
      </form>
    </div>
  );
}
