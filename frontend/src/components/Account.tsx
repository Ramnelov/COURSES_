import React, { useState, useContext, useEffect } from "react";
import { AuthContext } from "../context/AuthContext";
import { UserContext } from "../context/UserContext";

import { Link, useNavigate } from "react-router-dom";
import { updateUser, deleteUser } from "../services/api";
import { NotificationContext } from "../context/NotificationContext";
import { ProgressBar } from "react-bootstrap";
import { CSSTransition } from "react-transition-group";
import "../styles/App.scss";
import {
  validateUsername,
  validatePassword,
  validateEmail,
  validateConfirmPassword,
} from "../services/validation";

function AccountComponent() {
  const { setNotification } = useContext(NotificationContext);

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [usernameTouched, setUsernameTouched] = useState(false);
  const [emailTouched, setEmailTouched] = useState(false);
  const [passwordTouched, setPasswordTouched] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState(0);

  const { user, setUser } = useContext(UserContext);

  const { loggedIn, setLoggedIn } = useContext(AuthContext);

  const [confirmPassword, setConfirmPassword] = useState("");
  const [confirmPasswordTouched, setConfirmPasswordTouched] = useState(false);

  useEffect(() => {
    const storedUsername = localStorage.getItem("username");
    const storedEmail = localStorage.getItem("email");

    if (storedUsername) {
      setUsername(storedUsername);
    }

    if (storedEmail) {
      setEmail(storedEmail);
    }
  }, []);

  useEffect(() => {
    let strength = 0;
    if (/[a-z]/.test(password)) strength++; // Lowercase letter
    if (/[A-Z]/.test(password)) strength++; // Uppercase letter
    if (/[0-9]/.test(password)) strength++; // Number
    if (/[@$!%*?&]/.test(password)) strength++; // Special character
    if (password.length >= 8) strength++; // Minimum length
    setPasswordStrength(strength * 20); // Update password strength
  }, [password]);

  const handleUsernameSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    // Code to update username goes here
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await updateUser(token, username, null, null);
        console.log("User updated");
        const newToken = response;
        if (newToken) {
          console.log("Got new token");
          localStorage.setItem("token", newToken);
        }

        localStorage.setItem("username", username);
        if (user) {
          setUser({ ...user, username: username });
        }
        try {
          console.log("Getting new token");
          console.log(username);
        } catch (error) {
          const err = error as any;
          if (err.status === 401) {
            logOut();
          } else {
            setNotification("Something went wrong.", "danger");
            console.error(error);
          }
        }
        setNotification("Username successfully updated", "success");
      } catch (error) {
        const err = error as any;
        if (err.status === 401) {
          logOut();
        } else {
          setError("Something went wrong.");
          console.error(error);
        }
      }
    }
  };

  const handleEmailSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    // Code to update username goes here
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await updateUser(token, null, null, email);

        const newToken = response;
        if (newToken) {
          console.log("Got new token");
          localStorage.setItem("token", newToken);
        }

        localStorage.setItem("email", email);
        if (user) {
          setUser({ ...user, email: email });
        }
        setNotification("Email successfully updated", "success");
      } catch (error) {
        const err = error as any;
        if (err.status === 401) {
          logOut();
        } else {
          setError("Something went wrong.");
          console.error(error);
        }
      }
    }
  };

  const handlePasswordSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    // Code to update username goes here
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await updateUser(token, null, password, null);
        const newToken = response;
        if (newToken) {
          console.log("Got new token");
          localStorage.setItem("token", newToken);
        }
        setNotification("Password successfully updated", "success");
      } catch (error) {
        const err = error as any;
        if (err.status === 401) {
          logOut();
        } else {
          setError("Something went wrong.");
          console.error(error);
        }
      }
    }
  };

  const logOut = () => {
    setLoggedIn(false);
    setUser(null);
    localStorage.removeItem("token");
    localStorage.setItem("loggedIn", "false");
    localStorage.removeItem("username");
    localStorage.removeItem("email");
    localStorage.removeItem("role");
    setNotification("You were logged out.", "warning");
    navigate("/");
  };

  const deleteAccount = async () => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const response = await deleteUser(token);
        if (response === "User deleted") {
          setLoggedIn(false);
          setUser(null);
          localStorage.removeItem("loggedIn"); // Remove the loggedIn status from local storage
          localStorage.removeItem("token"); // Remove the token from local storage
          localStorage.removeItem("username");
          localStorage.removeItem("email");
          localStorage.removeItem("role");
          setNotification("", "info");
          navigate("/");
          setNotification("User successfully deleted", "success");
        }
      } catch (error) {
        if (error instanceof Error) {
          setError(error.message);
          console.error(error);
        }
      }
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-start">
        <div className="col-12 col-sm-10 col-md-8 col-lg-7">
          <form>
            <div className="form-group row">
              <div className="col-5">
                <label>Username:</label>
                <input
                  type="text"
                  className={`form-control ${
                    !validateUsername(username) && usernameTouched
                      ? "is-invalid"
                      : ""
                  }`}
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  onFocus={() => setUsernameTouched(true)}
                />
              </div>
              <div className="col-2 d-flex align-items-end">
                <button
                  type="submit"
                  className="btn btn-primary mt-3"
                  disabled={!validateUsername(username)}
                  onClick={handleUsernameSubmit}
                >
                  Update
                </button>
              </div>
            </div>
            <div className="form-group row">
              <div className="col-5">
                <label>Email:</label>
                <input
                  type="email"
                  className={`form-control ${
                    !validateEmail(email) && emailTouched ? "is-invalid" : ""
                  }`}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  onFocus={() => setEmailTouched(true)}
                />
              </div>
              <div className="col-2 d-flex align-items-end">
                <button
                  type="submit"
                  className="btn btn-primary mt-3"
                  disabled={!validateEmail(email)}
                  onClick={handleEmailSubmit}
                >
                  Update
                </button>
              </div>
            </div>
            <div className="form-group row">
              <div className="col-5">
                <label>Password:</label>
                <input
                  type="password"
                  className={`form-control ${
                    !validatePassword(password) && passwordTouched
                      ? "is-invalid"
                      : ""
                  }`}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  onFocus={() => setPasswordTouched(true)}
                />
                <CSSTransition
                  in={passwordTouched}
                  timeout={300}
                  classNames="fade"
                  unmountOnExit
                >
                  <ProgressBar
                    now={passwordStrength}
                    variant={
                      passwordStrength < 50
                        ? "danger"
                        : passwordStrength < 100
                        ? "warning"
                        : "success"
                    }
                    style={{ marginTop: "5px" }}
                  />
                </CSSTransition>
              </div>
              <div className="col-2 d-flex align-items-start mt-2">
                <button
                  type="submit"
                  className="btn btn-primary mt-3"
                  disabled={
                    !validatePassword(password) ||
                    !validateConfirmPassword(confirmPassword, password)
                  }
                  onClick={handlePasswordSubmit}
                >
                  Update
                </button>
              </div>
            </div>
            <CSSTransition in={passwordTouched} timeout={300} classNames="move">
              <div className="form-group row">
                <div className="col-5">
                  <label>Confirm Password:</label>
                  <input
                    type="password"
                    className={`form-control ${
                      !validateConfirmPassword(confirmPassword, password) &&
                      confirmPasswordTouched
                        ? "is-invalid"
                        : ""
                    }`}
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    onFocus={() => setConfirmPasswordTouched(true)}
                  />
                </div>
              </div>
            </CSSTransition>
            {error && (
              <div className="alert alert-danger mt-3 col-5">{error}</div>
            )}
            <CSSTransition in={passwordTouched} timeout={300} classNames="move">
              <div>
                <button
                  type="button"
                  className="btn btn-danger mt-3"
                  onClick={() => {
                    const confirmDelete = window.confirm(
                      "Are you sure you want to delete your account?"
                    );
                    if (confirmDelete) {
                      deleteAccount();
                    }
                  }}
                >
                  Delete Account
                </button>{" "}
              </div>
            </CSSTransition>
          </form>
        </div>
      </div>
    </div>
  );
}

export default AccountComponent;
