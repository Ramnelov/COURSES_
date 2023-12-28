// src/components/Navbar.tsx
import React, { useContext } from 'react';
import { Navbar, Nav, Button } from 'react-bootstrap';
import { NavLink, Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { NotificationContext } from '../context/NotificationContext';


const NavigationBar: React.FC = () => {

  const { setNotification } = useContext(NotificationContext);


  const { loggedIn, setLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    setLoggedIn(false);
    localStorage.removeItem('loggedIn'); // Remove the loggedIn status from local storage
    localStorage.removeItem('token'); // Remove the token from local storage
    setNotification('', 'info')
  };

  const handleLoginRedirect = () => {
    navigate('/');
  };

  return (
    <Navbar bg="dark" variant="dark">
      <Nav className="mr-auto">
        <NavLink className="nav-link" to="/" end>User application</NavLink>
      </Nav>
      <Navbar.Collapse className="justify-content-end">
          {loggedIn ? (
          <Button variant="outline-light" onClick={handleLogout} style={{ marginRight: '5px' }}>Log Out</Button>
        ) : (
          <Button variant="outline-light" onClick={handleLoginRedirect} style={{ marginRight: '5px' }}>Log In</Button>
        )}
      </Navbar.Collapse>
    </Navbar>
  );
}

export default NavigationBar;