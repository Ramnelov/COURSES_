import React, { useEffect, useState, useContext } from 'react';
import { getUser, User } from '../services/api';
import { AuthContext } from '../context/AuthContext';

function UserComponent() {
  const [user, setUser] = useState<User | null>(null);
  
  const { setLoggedIn } = useContext(AuthContext);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      getUser(token, setLoggedIn).then(data => setUser(data)).catch(error => console.error(error));
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