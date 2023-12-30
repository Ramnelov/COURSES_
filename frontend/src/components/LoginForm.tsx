import React, { useState, useContext } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Link } from "react-router-dom";
import { getToken } from "../services/api";
import { AuthContext } from "../context/AuthContext";
import { NotificationContext } from "../context/NotificationContext";
import { Spinner } from "react-bootstrap";

function LoginForm() {
  const [loading, setLoading] = useState(false);

  const { setNotification } = useContext(NotificationContext);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { setLoggedIn } = useContext(AuthContext);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    try {
      const token = await getToken(username, password);
      if (token) {
        localStorage.setItem("token", token);
        localStorage.setItem("loggedIn", "true");
        setLoggedIn(true);
        setNotification("", "info");
      }
    } catch (error) {
      const err = error as any;
      if (err.status === 401) {
        setError("Username or password is wrong.");
      } else {
        setNotification("Something went wrong.", "danger");
        console.error(error);
      }
    } finally {
      setLoading(false);
    }
    setUsername("");
    setPassword("");
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-8 col-sm-6 col-md-4 col-lg-3">
          {loading ? (
            <Spinner animation="border" role="status" />
          ) : (
            <div>
              <form onSubmit={handleSubmit} className="mt-5">
                <div className="form-group">
                  <label>Username:</label>
                  <input
                    type="text"
                    className="form-control"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                  />
                </div>
                <div className="form-group">
                  <label>Password:</label>
                  <input
                    type="password"
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                  />
                </div>
                {error && (
                  <div className="alert alert-danger mt-3">{error}</div>
                )}
                <button
                  type="submit"
                  className="btn btn-primary mt-3"
                  disabled={!username || !password}
                >
                  Login
                </button>
              </form>
              <div className="mt-3">
                No account? <Link to="/create">Create one</Link>
              </div>
              <div className="mt-3">
                Forgoten password?{" "}
                <Link to="/restore-password">Restore it</Link>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default LoginForm;
