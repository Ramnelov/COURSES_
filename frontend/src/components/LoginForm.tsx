import React, { useState, useContext } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
import { getToken } from '../services/api';
import { AuthContext } from '../context/AuthContext';

function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setLoggedIn } = useContext(AuthContext);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      const token = await getToken(username, password);
      if (token) {
        localStorage.setItem('token', token); 
        setLoggedIn(true);
        localStorage.setItem('loggedIn', 'true');
        
      }
    } catch (error) {
      const err = error as any;
      if (err.status === 401) {
        setError('Username or password is wrong');
      } else {
        console.error(error);
      }
    }
    setUsername('');
    setPassword('');
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
              <label>Password:</label>
              <input type="password" className="form-control" value={password} onChange={e => setPassword(e.target.value)} />
            </div>
            {error && <div className="alert alert-danger mt-3">{error}</div>}
            <button type="submit" className="btn btn-primary mt-3" disabled={!username || !password}>Login</button>
          </form>
          <div className="mt-3">
            No account? <Link to="/create">Create one</Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;