// src/services/api.ts

export interface User {
  username: string;
  role: string;
  email: string;
}

export async function getToken(
  username: string,
  password: string
): Promise<string> {
  const response = await fetch("http://localhost:8443/api/users/token", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw response;
  }

  return response.text();
}

export async function getUser(token: string): Promise<any> {
  const response = await fetch("http://localhost:8443/api/users/", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw response;
  }

  return response.json();
}

export async function deleteUser(token: string): Promise<any> {
  const response = await fetch("http://localhost:8443/api/users/", {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText);
  }

  return response.text();
}

export async function updateUser(
  token: string,
  username: string | null,
  password: string | null,
  email: string | null
): Promise<any> {
  const response = await fetch("http://localhost:8443/api/users/", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ username, password, email }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText);
  }

  return response.text();
}

export async function createUser(
  username: string,
  password: string,
  email: string
): Promise<any> {
  const response = await fetch("http://localhost:8443/api/users/", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password, email }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText);
  }

  return response.text();
}
