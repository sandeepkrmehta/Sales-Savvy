import React from 'react';
import { Navigate } from 'react-router-dom';
import { authService } from '../utils/auth';

export default function ProtectedRoute({ children, adminOnly = false }) {
  if (!authService.isAuthenticated()) {
    return <Navigate to="/sign_in" replace />;
  }

  if (adminOnly && !authService.isAdmin()) {
    return <Navigate to="/customer_home" replace />;
  }

  return children;
}
