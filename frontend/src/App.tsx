import React, { useContext } from 'react';
import logo from './logo.svg';
import './styles/App.scss';
import 'bootstrap/dist/css/bootstrap.css';
import NavigationBar from './components/Navbar'; // Import the Navbar component
import { Routes, Route } from 'react-router-dom';
import UserComponent from './components/User'; // Assuming SimpleComponent is in the same directory
import { AuthContext } from './context/AuthContext';
import LoginForm from './components/LoginForm';
import CreateUserForm from './components/CreateUserForm';
import TestComponent from './components/TestComponent';


function App() {
  const { loggedIn } = useContext(AuthContext);

  return (
    <div className="App">
      <NavigationBar /> 
      <Routes>
        <Route path="/" element={loggedIn ? <UserComponent /> : <LoginForm />} />
        <Route path="/create" element={<CreateUserForm/>} />
        <Route path="/test" element={<TestComponent/>} />
      </Routes>
    </div>
  );
}

export default App;
