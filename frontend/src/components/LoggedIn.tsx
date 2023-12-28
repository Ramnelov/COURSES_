import React, { useEffect, useState, useContext } from 'react';
import { getUser, User } from '../services/api';
import { AuthContext } from '../context/AuthContext';
import { Spinner } from 'react-bootstrap';

function UserComponent() {
  const [user, setUser] = useState<User | null>(null);
  
  const { setLoggedIn } = useContext(AuthContext);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {


      getUser(token)
        .then(data => setUser(data))
        .catch(error => {
          console.error(error);
          if (error.status === 401) {
            setLoggedIn(false);
            localStorage.removeItem('token');
            localStorage.setItem('loggedIn', 'false');
          }
        });
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
        <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
        <Spinner animation="border" role="status" />
      </div>
      )}
    </div>
  );
}

export default UserComponent;