import React from 'react';
import logo from './logo.svg';
import './styles/App.scss';
import 'bootstrap/dist/css/bootstrap.css';
import NavigationBar from './components/Navbar'; // Import the Navbar component
import { Routes, Route } from 'react-router-dom';
import SimpleComponent from './components/SimpleComponent'; // Assuming SimpleComponent is in the same directory
import { AuthProvider } from './context/AuthContext';
import LoginForm from './components/LoginForm';


function App() {
  return (
    <div className="App">
      <NavigationBar /> 
      <Routes>
        <Route path="/" element={<LoginForm />} />
        <Route path="/features" element={<SimpleComponent />} />
        <Route path="/pricing" element={<SimpleComponent />} />
      </Routes>
    </div>
  );
}

export default App;
