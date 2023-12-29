import React, { createContext, useState } from "react";

interface NotificationContextProps {
  message: string;
  type: "success" | "danger" | "warning" | "info";
  setNotification: (
    message: string,
    type: "success" | "danger" | "warning" | "info"
  ) => void;
}

interface NotificationProviderProps {
  children: React.ReactNode;
}

export const NotificationContext = createContext<NotificationContextProps>({
  message: "",
  type: "info",
  setNotification: () => {},
});

export const NotificationProvider: React.FC<NotificationProviderProps> = ({
  children,
}) => {
  const [message, setMessage] = useState("");
  const [type, setType] = useState<"success" | "danger" | "warning" | "info">(
    "info"
  );

  const setNotification = (
    message: string,
    type: "success" | "danger" | "warning" | "info"
  ) => {
    setMessage(message);
    setType(type);
  };

  return (
    <NotificationContext.Provider value={{ message, type, setNotification }}>
      {children}
    </NotificationContext.Provider>
  );
};
