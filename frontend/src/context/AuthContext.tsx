import React, { createContext, useState, useEffect, ReactNode } from 'react';

interface AuthContextProps {
  loggedIn: boolean;
  setLoggedIn: React.Dispatch<React.SetStateAction<boolean>>;
}

export const AuthContext = createContext<AuthContextProps>({ loggedIn: false, setLoggedIn: () => {} });

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  // Initialize state from localStorage
  const [loggedIn, setLoggedIn] = useState(() => {
    const storedLoggedIn = localStorage.getItem('loggedIn');
    return storedLoggedIn ? JSON.parse(storedLoggedIn) : false;
  });

  // Update localStorage whenever loggedIn changes
  useEffect(() => {
    localStorage.setItem('loggedIn', JSON.stringify(loggedIn));
  }, [loggedIn]);

  return (
    <AuthContext.Provider value={{ loggedIn, setLoggedIn }}>
      {children}
    </AuthContext.Provider>
  );
};