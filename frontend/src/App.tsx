import React, { useContext } from 'react';
import logo from './logo.svg';
import './styles/App.scss';
import 'bootstrap/dist/css/bootstrap.css';
import NavigationBar from './components/Navbar'; // Import the Navbar component
import { Routes, Route } from 'react-router-dom';
import LoggedInComponent from './components/LoggedIn'; // Assuming SimpleComponent is in the same directory
import { AuthContext } from './context/AuthContext';
import LoginForm from './components/LoginForm';
import CreateUserForm from './components/CreateUserForm';
import TestComponent from './components/TestComponent';
import { NotificationProvider } from './context/NotificationContext';
import { Notification } from './components/Notification';



function App() {
  const { loggedIn } = useContext(AuthContext);





  return (
    <NotificationProvider>
      
      <div className="App">
      <div className="header">
    <NavigationBar />
    <div className="notification-wrapper">
      <Notification />
    </div>
  </div>
        <div className="main-content">
        <Routes>
          <Route path="/" element={loggedIn ? <LoggedInComponent /> : <LoginForm />} />
          <Route path="/create" element={<CreateUserForm/>} />
          <Route path="/test" element={<TestComponent/>} />
         </Routes>
        </div>
      </div>
    </NotificationProvider>
  );
}

export default App;
