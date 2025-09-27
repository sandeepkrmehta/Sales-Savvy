import React, { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';

export default function Navigation() {
  const navigate = useNavigate();
  const location = useLocation();
  const username = localStorage.getItem('username');
  const userRole = localStorage.getItem('userRole');
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleLogout = () => {
    localStorage.removeItem('username');
    localStorage.removeItem('userRole');
    localStorage.removeItem('rememberMe');
    navigate('/');
    setIsMenuOpen(false);
  };

  const handleLogin = () => {
    navigate('/sign_in_page', { state: { from: location.pathname } });
    setIsMenuOpen(false);
  };

  const handleSignup = () => {
    navigate('/sign_up_page');
    setIsMenuOpen(false);
  };

  const handleCart = () => {
    if (username) {
      navigate('/view_cart_page');
    } else {
      alert('Please login to view your cart!');
      navigate('/sign_in_page', { state: { from: '/view_cart_page' } });
    }
    setIsMenuOpen(false);
  };

  const handleAdminPanel = () => {
    navigate('/admin_page');
    setIsMenuOpen(false);
  };

  return (
    <nav className="navbar">
      <div className="nav-container">
        {/* Logo and Brand */}
        <div className="nav-brand">
          <Link to="/" className="brand-link">
            <span className="logo">🛍️</span>
            <span className="brand-name">Sales-Savvy</span>
          </Link>
        </div>

        {/* Desktop Navigation */}
        <div className="nav-menu desktop-menu">
          <Link to="/" className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}>
            Home
          </Link>
          <a href="#about" className="nav-link">About Us</a>
          <a href="#contact" className="nav-link">Contact</a>
          
          {username ? (
            <div className="user-section">
              <span className="welcome-text">
                Welcome, {username} 
                <span className="user-role">({userRole})</span>
              </span>
              
              {/* Show Admin Panel button only for admin users */}
              {userRole === 'admin' && (
                <button className="nav-btn admin-btn" onClick={handleAdminPanel}>
                  ⚙️ Admin Panel
                </button>
              )}
              
              {/* Show Cart button only for customers */}
              {userRole === 'customer' && (
                <button className="nav-btn cart-btn" onClick={handleCart}>
                  🛒 Cart
                </button>
              )}
              
              <button className="nav-btn logout-btn" onClick={handleLogout}>
                Logout
              </button>
            </div>
          ) : (
            <div className="auth-section">
              <button className="nav-btn login-btn" onClick={handleLogin}>
                Login
              </button>
              <button className="nav-btn signup-btn" onClick={handleSignup}>
                Sign Up
              </button>
            </div>
          )}
        </div>

        {/* Mobile Menu Button */}
        <button 
          className="mobile-menu-btn"
          onClick={() => setIsMenuOpen(!isMenuOpen)}
        >
          ☰
        </button>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="nav-menu mobile-menu">
            <Link to="/" className="nav-link" onClick={() => setIsMenuOpen(false)}>
              Home
            </Link>
            <a href="#about" className="nav-link" onClick={() => setIsMenuOpen(false)}>About Us</a>
            <a href="#contact" className="nav-link" onClick={() => setIsMenuOpen(false)}>Contact</a>
            
            {username ? (
              <>
                <span className="welcome-text-mobile">
                  Welcome, {username} ({userRole})
                </span>
                
                {userRole === 'admin' && (
                  <button className="nav-btn" onClick={handleAdminPanel}>⚙️ Admin Panel</button>
                )}
                
                {userRole === 'customer' && (
                  <button className="nav-btn" onClick={handleCart}>🛒 Cart</button>
                )}
                
                <button className="nav-btn" onClick={handleLogout}>Logout</button>
              </>
            ) : (
              <>
                <button className="nav-btn" onClick={handleLogin}>Login</button>
                <button className="nav-btn" onClick={handleSignup}>Sign Up</button>
              </>
            )}
          </div>
        )}
      </div>
    </nav>
  );
}