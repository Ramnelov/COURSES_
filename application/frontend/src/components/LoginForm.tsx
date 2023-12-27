import React, { useState, useContext } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { getToken } from '../services/api';
import { AuthContext } from '../context/AuthContext';


function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { setLoggedIn } = useContext(AuthContext);


  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      const token = await getToken(username, password);
      if (token) {
        localStorage.setItem('token', token); 
        setLoggedIn(true);
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-2">
          <form onSubmit={handleSubmit} className="mt-5">
            <div className="form-group">
              <label>Username:</label>
              <input type="text" className="form-control" value={username} onChange={e => setUsername(e.target.value)} />
            </div>
            <div className="form-group">
              <label>Password:</label>
              <input type="password" className="form-control" value={password} onChange={e => setPassword(e.target.value)} />
            </div>
            <button type="submit" className="btn btn-primary">Login</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;