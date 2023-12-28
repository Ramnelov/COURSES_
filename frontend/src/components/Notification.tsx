import React, { useContext } from 'react';
import { Alert } from 'react-bootstrap';
import { NotificationContext } from '../context/NotificationContext';

export const Notification: React.FC = () => {
    const { message, type, setNotification } = useContext(NotificationContext);
  
    if (!message) {
      return null;
    }
  
    return (
      <Alert variant={type} dismissible onClose={() => setNotification('', 'info')}>
        {message}
      </Alert>
    );
  };