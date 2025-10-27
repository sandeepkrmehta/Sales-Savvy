import React from 'react';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-dark text-light py-3 mt-auto">
      <div className="container text-center">
        <p className="mb-0">
          &copy; {currentYear} <strong>SalesSavvy</strong>. All rights reserved.
        </p>
      </div>
    </footer>
  );
};

export default Footer;
