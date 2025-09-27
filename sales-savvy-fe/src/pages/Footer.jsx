import React from 'react';

export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <p>&copy; 2024 Sales-Savvy. All rights reserved.</p>
        <div className="footer-links">
          <a href="/about">About Us</a>
          <a href="/contact">Contact</a>
          <a href="/privacy">Privacy Policy</a>
          <a href="/terms">Terms of Service</a>
        </div>
      </div>
    </footer>
  );
}