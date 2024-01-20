// src/components/Navbar.tsx
import React, { useContext } from "react";
import { Navbar, Nav, Button, Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { NotificationContext } from "../context/NotificationContext";
import { UserContext } from "../context/UserContext";
import "../styles/App.scss";

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
    localStorage.removeItem("username");
    localStorage.removeItem("email");
    localStorage.removeItem("role");
    setNotification("", "info");
    navigate("/");
  };

  const handleLoginRedirect = () => {
    navigate("/");
  };

  return (
    <Navbar bg="dark" variant="dark">
      <Container style={{ maxWidth: "1200px" }}>
        <Nav className="mr-auto">
          <Navbar.Brand>
            <img
              src="logo-nav.png" // Replace with the path to your image
              alt="courses"
              style={{ width: "85px", cursor: "pointer", marginLeft: "10px" }} // Adjust size as needed
              onClick={() => navigate("/")}
            />
          </Navbar.Brand>
          {loggedIn &&
            (user?.role || localStorage.getItem("role")) === "USER" && (
              <>
                <Navbar.Text
                  className="text-white"
                  onClick={() => navigate("/")}
                  style={{ cursor: "pointer" }}
                >
                  Home
                </Navbar.Text>

                <Navbar.Text
                  className="text-white"
                  onClick={() => navigate("/account")}
                  style={{ marginLeft: "10px", cursor: "pointer" }}
                >
                  Account
                </Navbar.Text>
              </>
            )}
          {loggedIn &&
            (user?.role || localStorage.getItem("role")) === "ADMIN" && (
              <>
                <Navbar.Text
                  className="text-white"
                  onClick={() => navigate("/")}
                  style={{ cursor: "pointer" }}
                >
                  Home
                </Navbar.Text>

                <Navbar.Text
                  className="text-white"
                  onClick={() => navigate("/account")}
                  style={{ marginLeft: "10px", cursor: "pointer" }}
                >
                  Account
                </Navbar.Text>
                <Navbar.Text
                  className="text-white"
                  onClick={() => navigate("/admin")}
                  style={{ marginLeft: "10px", cursor: "pointer" }}
                >
                  Admin
                </Navbar.Text>
              </>
            )}
        </Nav>
        <Navbar.Collapse className="justify-content-end">
          {loggedIn ? (
            <>
              <Navbar.Text
                className="text-white"
                style={{ marginRight: "10px" }}
              >
                {user?.username || localStorage.getItem("username")}
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
      </Container>
    </Navbar>
  );
};

export default NavigationBar;
