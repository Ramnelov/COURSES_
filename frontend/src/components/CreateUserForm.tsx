import React, { useState, useContext, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { createUser } from "../services/api";
import { NotificationContext } from "../context/NotificationContext";
import { ProgressBar } from "react-bootstrap";
import { CSSTransition } from "react-transition-group";
import "../styles/App.scss";
import { validateUsername, validatePassword, validateEmail, validateConfirmPassword } from "../services/validation";

function CreateUserForm() {
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

  const [confirmPassword, setConfirmPassword] = useState("");
  const [confirmPasswordTouched, setConfirmPasswordTouched] = useState(false);

  useEffect(() => {
    let strength = 0;
    if (/[a-z]/.test(password)) strength++; // Lowercase letter
    if (/[A-Z]/.test(password)) strength++; // Uppercase letter
    if (/[0-9]/.test(password)) strength++; // Number
    if (/[@$!%*?&]/.test(password)) strength++; // Special character
    if (password.length >= 8) strength++; // Minimum length
    setPasswordStrength(strength * 20); // Update password strength
  }, [password]);

  

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      const response = await createUser(username, password, email);
      if (response === "Registration successful") {
        setNotification("User successfully created", "success");
        navigate("/");
      }
    } catch (error) {
      if (error instanceof Error) {
        setError(error.message);
        console.error(error);
      }
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-8 col-sm-6 col-md-4 col-lg-3">
          <form onSubmit={handleSubmit}>
            <div className="form-group">
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
            <div className="form-group">
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
            <div className="form-group">
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
            <CSSTransition in={passwordTouched} timeout={300} classNames="move">
            <div className="form-group">
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
            </CSSTransition>
            {error && <div className="alert alert-danger mt-3">{error}</div>}
            <CSSTransition in={passwordTouched} timeout={300} classNames="move">
              <div>
                <button
                  type="submit"
                  className="btn btn-primary mt-3"
                  disabled={
                    !validateUsername(username) ||
                    !validateEmail(email) ||
                    !validatePassword(password) ||
                    !validateConfirmPassword(confirmPassword, password)
                  }
                >
                  Create Account
                </button>
                <div className="mt-3">
                  Already have an account? <Link to="/">Login</Link>
                </div>
              </div>
            </CSSTransition>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CreateUserForm;
