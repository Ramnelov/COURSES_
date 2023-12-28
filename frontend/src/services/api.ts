// src/services/api.ts
import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';


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
    throw response;
  }

  return response.text();
}

export async function getUser(token: string): Promise<any> {
  const response = await fetch('http://localhost:8443/api/users/', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
  });

   if (!response.ok) {
    throw response;
  }

  return response.json();
}

export async function createUser(username: string, password: string, email: string): Promise<any> {
  const response = await fetch('http://localhost:8443/api/users/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password, email }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText);
  }

  return response.text();
}