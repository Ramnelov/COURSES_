import React, { useEffect, useState, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { Spinner } from "react-bootstrap";
import { NotificationContext } from "../context/NotificationContext";
import { UserContext } from "../context/UserContext";

function AccountComponent() {

    const { user, setUser } = useContext(UserContext);
    const { setLoggedIn } = useContext(AuthContext);
    const { setNotification } = useContext(NotificationContext);


    return <div>Test</div>;
  }
  
  export default AccountComponent;