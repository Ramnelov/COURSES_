// src/services/api.ts

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