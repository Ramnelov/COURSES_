import React, { useContext, useState } from "react";
import "./styles/App.scss";
import "bootstrap/dist/css/bootstrap.css";
import NavigationBar from "./components/Navbar"; // Import the Navbar component
import { Routes, Route, useLocation } from "react-router-dom";
import LoggedInComponent from "./components/LoggedIn"; // Assuming SimpleComponent is in the same directory
import { AuthContext } from "./context/AuthContext";
import LoginForm from "./components/LoginForm";
import CreateUserForm from "./components/CreateUserForm";
import TestComponent from "./components/TestComponent";
import { NotificationProvider } from "./context/NotificationContext";
import { Notification } from "./components/Notification";
import { User } from "./services/api";
import { UserContext } from "./context/UserContext";
import AccountComponent from "./components/Account";
import AdminComponent from "./components/Admin";

function App() {
  const { loggedIn } = useContext(AuthContext);
  // Create a UserContext

  // In your parent component
  const [user, setUser] = useState<User | null>(null);

  const location = useLocation();

  return (
    <NotificationProvider>
      <UserContext.Provider value={{ user, setUser }}>
        <div className="App">
          <div className="header">
            <NavigationBar />
            <div className="notification-wrapper">
              <Notification />
            </div>
          </div>
          <div>
            <Routes>
              <Route
                path="/"
                element={
                  loggedIn ? (
                    <div className="main-content">
                      <LoggedInComponent />
                    </div>
                  ) : (
                    <div className="main-content-center">
                      <LoginForm />
                    </div>
                  )
                }
              />
              <Route
                path="/create"
                element={
                  <div className="main-content-center">
                    <CreateUserForm />
                  </div>
                }
              />
              <Route
                path="/test"
                element={
                  <div className="main-content">
                    <TestComponent />
                  </div>
                }
              />
              <Route
                path="/account"
                element={
                  <div className="main-content">
                    <AccountComponent />
                  </div>
                }
              />
              <Route
                path="/admin"
                element={
                  <div className="main-content">
                    <AdminComponent />
                  </div>
                }
              />
            </Routes>
          </div>
        </div>
      </UserContext.Provider>
    </NotificationProvider>
  );
}

export default App;
