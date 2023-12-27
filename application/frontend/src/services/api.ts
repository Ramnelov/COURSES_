// src/services/api.ts

export interface User {
  username: string;
  role: string;
  email: string;
}

export async function getToken(username: string, password: string): Promise<string> {
  const response = await fetch('http://localhost:8443/api/users/token', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  return response.text(); // Return the response as a string
}

export async function getUsers(token: string): Promise<any> {
  const response = await fetch('http://localhost:8443/api/users/', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
  });

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  return response.json(); // Assuming the response data is JSON
}



