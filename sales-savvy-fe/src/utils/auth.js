const TOKEN_KEY = 'jwt_token';
const USERNAME_KEY = 'username';
const ROLE_KEY = 'role';

export const authService = {
  setAuth(token, username, role) {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USERNAME_KEY, username);
    localStorage.setItem(ROLE_KEY, role);
  },

  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },

  getUsername() {
    return localStorage.getItem(USERNAME_KEY);
  },

  getRole() {
    return localStorage.getItem(ROLE_KEY);
  },

  isAuthenticated() {
    return !!this.getToken();
  },

  isAdmin() {
    return this.getRole()?.toLowerCase() === 'admin';
  },

  isCustomer() {
    return this.getRole()?.toLowerCase() === 'customer';
  },

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USERNAME_KEY);
    localStorage.removeItem(ROLE_KEY);
  },

  getAuthHeaders() {
    const token = this.getToken();
    return {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    };
  }
};
