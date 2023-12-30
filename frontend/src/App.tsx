import React, { useContext, useState } from "react";
import "./styles/App.scss";
import "bootstrap/dist/css/bootstrap.css";
import NavigationBar from "./components/Navbar"; // Import the Navbar component
import { Routes, Route } from "react-router-dom";
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
          <div className="main-content">
            <Routes>
              <Route
                path="/"
                element={loggedIn ? <LoggedInComponent /> : <LoginForm />}
              />
              <Route path="/create" element={<CreateUserForm />} />
              <Route path="/test" element={<TestComponent />} />
              <Route path="/account" element={<AccountComponent />} />
              <Route path="/admin" element={<AdminComponent />} />
            </Routes>
          </div>
        </div>
      </UserContext.Provider>
    </NotificationProvider>
  );
}

export default App;
