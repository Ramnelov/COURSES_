import React, { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { createUser } from '../services/api';
import { AuthContext } from '../context/AuthContext';

function CreateUserForm() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setLoggedIn } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      const user = await createUser(username, password, email);
      if (user) {
        navigate('/');
      }
    } catch (error) {
      setError('Failed to create user');
      console.error(error);
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
            <button type="submit" className="btn btn-primary mt-3" disabled={!username || !email || !password}>Create Account</button>
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