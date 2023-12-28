import React, { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { createUser } from '../services/api';
import { AuthContext } from '../context/AuthContext';
import { NotificationContext } from '../context/NotificationContext';

function CreateUserForm() {

  const { setNotification } = useContext(NotificationContext);

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();

  function validateEmail(email: string) {
    var re = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    return re.test(email);
  }

  function validateUsername(username: string) {
    var re = /^[a-zA-Z0-9]{3,20}$/;
    return re.test(username);
  }

  function validatePassword(password: string) {
    var re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return re.test(password);
  }

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      const response = await createUser(username, password, email);
      if (response === "Registration successful") {
        setNotification('User successfully created', 'success');
        navigate('/');
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
        <div className="col-3">
          <form onSubmit={handleSubmit} className="mt-5">
            <div className="form-group">
              <label>Username:</label>
              <input type="text" className="form-control" value={username} onChange={e => setUsername(e.target.value)} />
            </div>
            <div className="form-group">
              <label>Email:</label>
              <input type="email" className="form-control" value={email} onChange={e => setEmail(e.target.value)} />
            </div>
            <div className="form-group">
              <label>Password:</label>
              <input type="password" className="form-control" value={password} onChange={e => setPassword(e.target.value)} />
            </div>
            {error && <div className="alert alert-danger mt-3">{error}</div>}
            <button type="submit" className="btn btn-primary mt-3" disabled={!validateUsername(username) || !validateEmail(email) || !validatePassword(password)}>Create Account</button>
          </form>
          <div className="mt-3">
            Already have an account? <Link to="/">Login</Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CreateUserForm;