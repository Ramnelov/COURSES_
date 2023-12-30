import React, { useEffect, useState, useContext } from "react";
import { getUser } from "../services/api";
import { AuthContext } from "../context/AuthContext";
import { Spinner } from "react-bootstrap";
import { NotificationContext } from "../context/NotificationContext";
import { UserContext } from "../context/UserContext";

const LoggedIn: React.FC = () => {
  const { user, setUser } = useContext(UserContext);
  const { setLoggedIn } = useContext(AuthContext);
  const { setNotification } = useContext(NotificationContext);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      getUser(token)
        .then((data) => {
          console.log(data);
          setUser(data);
          localStorage.setItem("username", data.username);
          localStorage.setItem("email", data.email);
          localStorage.setItem("role", data.role);
        })
        .catch((error) => {
          console.error(error);
          if (error.status === 401) {
            setLoggedIn(false);
            setUser(null);
            localStorage.removeItem("token");
            localStorage.setItem("loggedIn", "false");
            localStorage.removeItem("username")
            localStorage.removeItem("email")
            localStorage.removeItem("role")
            setNotification("You were logged out.", "warning");
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
        <div
          className="d-flex justify-content-center align-items-center"
          style={{ height: "100vh" }}
        >
          <Spinner animation="border" role="status" />
        </div>
      )}
    </div>
  );
};

export default LoggedIn;
