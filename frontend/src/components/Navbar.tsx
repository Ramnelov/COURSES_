// src/components/Navbar.tsx
import React, { useState, useEffect, useContext } from "react";
import { Navbar, Nav, Button } from "react-bootstrap";
import { NavLink, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { NotificationContext } from "../context/NotificationContext";
import { UserContext } from "../context/UserContext";

const NavigationBar: React.FC = () => {
  const { setNotification } = useContext(NotificationContext);

  const { user, setUser } = useContext(UserContext);

  const { loggedIn, setLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    setLoggedIn(false);
    setUser(null);
    localStorage.removeItem("loggedIn"); // Remove the loggedIn status from local storage
    localStorage.removeItem("token"); // Remove the token from local storage
    localStorage.removeItem("username"); // Remove the username from local storage
    localStorage.removeItem("role"); // Remove the role from local storage
    localStorage.removeItem("email"); // Remove the email from local storage
    setNotification("", "info");
  };

  const handleLoginRedirect = () => {
    navigate("/");
  };

  return (
    <Navbar bg="dark" variant="dark">
      <Nav className="mr-auto">
        <Navbar.Brand>
          <img
            src="logo-nav.png" // Replace with the path to your image
            alt="courses"
            style={{ width: "85px", cursor: "pointer" }} // Adjust size as needed
            onClick={() => navigate("/")}
          />
        </Navbar.Brand>
        {loggedIn && (
          <Navbar.Text
            className="text-white"
            onClick={() => navigate("/account")}
            style={{ cursor: "pointer" }}
          >
            Account
          </Navbar.Text>
        )}
        {loggedIn && user?.role === "ADMIN" && (
          <Navbar.Text
            className="text-white"
            onClick={() => navigate("/admin")}
            style={{ marginLeft: "10px", cursor: "pointer" }}
          >
            Admin
          </Navbar.Text>
        )}
      </Nav>
      <Navbar.Collapse className="justify-content-end">
        {loggedIn ? (
          <>
            <Navbar.Text className="text-white" style={{ marginRight: "10px" }}>
              {user?.username}
            </Navbar.Text>
            <Button
              variant="outline-light"
              onClick={handleLogout}
              style={{ marginRight: "10px" }}
            >
              Log Out
            </Button>
          </>
        ) : (
          <Button
            variant="outline-light"
            onClick={handleLoginRedirect}
            style={{ marginRight: "10px" }}
          >
            Log In
          </Button>
        )}
      </Navbar.Collapse>
    </Navbar>
  );
};

export default NavigationBar;
