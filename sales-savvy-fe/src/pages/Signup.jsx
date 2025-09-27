import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function Signup() {
  const [username, setUsername] = useState('');
  const [email, setEmail]     = useState('');
  const [password, setPassword] = useState('');
  const [dob, setDob]         = useState('');
  const [gender, setGender]   = useState('');
  const [role, setRole]       = useState('');
  const [errors, setErrors]   = useState({});
  const navigate = useNavigate();

  const validate = () => {
    const errs = {};
    if (!username.trim()) errs.username = 'Username is required';
    if (!email.trim()) errs.email = 'Email is required';
    else if (!/^\S+@\S+\.\S+$/.test(email)) errs.email = 'Email is invalid';
    if (!password.trim()) errs.password = 'Password is required';
    else if (password.length < 5) errs.password = 'Password must be at least 5 characters';
    if (!dob) errs.dob = 'Date of birth is required';
    if (!gender) errs.gender = 'Gender is required';
    if (!role) errs.role = 'Role is required';
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
      .post('http://localhost:8080/signUp', { username, email, password, dob, gender, role })
      .then(res => {
        alert('Signed up: ' + res.data);
        navigate('/sign_in_page');
      })
      .catch(err => {
        console.error(err);
        alert('Error signing up – check console');
      });
  };

  return (
    <div className="container">
      <form className="form-container" onSubmit={handleSubmit}>
        <h2>Sign up below:</h2>

        <div className="form-group">
          <label>Username:<sup style={{ color: 'red' }}>*</sup></label>
          <input type="text" value={username} onChange={e=>setUsername(e.target.value)}  required />
          {errors.username && <p className="error">{errors.username}</p>}
        </div>

        <div className="form-group">
          <label>Email:<sup style={{ color: 'red' }}>*</sup></label>
          <input type="email" value={email} onChange={e=>setEmail(e.target.value)} required/>
          {errors.email && <p className="error">{errors.email}</p>}
        </div>

        <div className="form-group">
          <label>Password:<sup style={{ color: 'red' }}>*</sup></label>
          <input type="password" value={password} onChange={e=>setPassword(e.target.value)} required />
          {errors.password && <p className="error">{errors.password}</p>}
        </div>

        <div className="form-group">
          <label>Gender:<sup style={{ color: 'red' }}>*</sup></label>
          <label><input type="radio" name="gender" value="MALE"   onChange={e=>setGender(e.target.value)} /> Male</label>
          <label><input type="radio" name="gender" value="FEMALE" onChange={e=>setGender(e.target.value)} /> Female</label>
          <label><input type="radio" name="gender" value="OTHER"  onChange={e=>setGender(e.target.value)} /> Other</label>
          {errors.gender && <p className="error">{errors.gender}</p>}
        </div>

        <div className="form-group">
          <label>Date of Birth:<sup style={{ color: 'red' }}>*</sup></label>
          <input type="date" value={dob} onChange={e=>setDob(e.target.value)} />
          {errors.dob && <p className="error">{errors.dob}</p>}
        </div>

        <div className="form-group">
          <label>Role:<sup style={{ color: 'red' }}>*</sup></label>
          <label><input type="radio" name="role" value="ADMIN"    onChange={e=>setRole(e.target.value)} /> Admin</label>
          <label><input type="radio" name="role" value="CUSTOMER" onChange={e=>setRole(e.target.value)} /> Customer</label>
          {errors.role && <p className="error">{errors.role}</p>}
        </div>

        <button className="btn btn-primary" type="submit">Sign Up</button>
      </form>
    </div>
  );
}
