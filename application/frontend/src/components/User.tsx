import React, { useEffect, useState } from 'react';
import { getUsers, User } from '../services/api';

function UserComponent() {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      getUsers(token).then(data => setUser(data)).catch(error => console.error(error));
    }
  }, []);

  return (
    <div>
      {user ? (
        <>
          <h1>Hello, {user.username}!</h1>
          <p>Your role is: {user.role}</p>
          <p>Your email is: {user.email}</p>
        </>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
}

export default UserComponent;