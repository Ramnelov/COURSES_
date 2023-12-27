import React from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import { NavLink, Link } from 'react-router-dom';

const NavigationBar: React.FC = () => {
  return (
    <Navbar bg="dark" variant="dark">
      <Link to="/" className="navbar-brand">User application</Link>
      <Nav className="mr-auto">
        <NavLink className="nav-link" to="/" end>Home</NavLink>
        
    </Nav>
    </Navbar>
  );
}

export default NavigationBar;