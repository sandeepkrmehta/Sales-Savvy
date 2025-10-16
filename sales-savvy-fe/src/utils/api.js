import { authService } from './auth';

const API_BASE_URL = 'http://localhost:8080';

async function fetchWithAuth(url, options = {}) {
  const config = {
    ...options,
    headers: {
      ...authService.getAuthHeaders(),
      ...options.headers,
    },
  };

  const response = await fetch(`${API_BASE_URL}${url}`, config);

  if (response.status === 401 || response.status === 403) {
    authService.logout();
    window.location.href = '/sign_in';
    throw new Error('Unauthorized');
  }

  return response;
}

export const api = {
  get(url) {
    return fetchWithAuth(url);
  },

  post(url, data) {
    return fetchWithAuth(url, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  put(url, data) {
    return fetchWithAuth(url, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  delete(url) {
    return fetchWithAuth(url, {
      method: 'DELETE',
    });
  },
};
