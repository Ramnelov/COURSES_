// src/components/Navbar.tsx
import React, { useContext } from 'react';
import { Navbar, Nav, Button } from 'react-bootstrap';
import { NavLink, Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const NavigationBar: React.FC = () => {
  const { loggedIn, setLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    setLoggedIn(false);
    localStorage.removeItem('token'); // Remove the token from local storage
  };

  const handleLoginRedirect = () => {
    navigate('/');
  };

  return (
    <Navbar bg="dark" variant="dark">
      <Link to="/" className="navbar-brand">User application</Link>
      <Nav className="mr-auto">
        <NavLink className="nav-link" to="/" end>Home</NavLink>
      </Nav>
      <Navbar.Collapse className="justify-content-end">
        {loggedIn ? (
          <Button variant="outline-info" onClick={handleLogout}>Log Out</Button>
        ) : (
          <Button variant="outline-info" onClick={handleLoginRedirect}>Log In</Button>
        )}
      </Navbar.Collapse>
    </Navbar>
  );
}

export default NavigationBar;