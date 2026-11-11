import React from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AuthProvider, { RequireAuth } from "./auth/AuthContext";
import ToastProvider from "./ui/Toast";
import { MuiTheme } from "./ui/theme";
import Shell from "./ui/Shell";
import LoginScreen from "./screens/LoginScreen";
import DashboardScreen from "./screens/DashboardScreen";
import EventsScreen from "./screens/EventsScreen";
import NotificationsScreen from "./screens/NotificationsScreen";
import NotFoundScreen from "./screens/NotFoundScreen";

const Private = ({ children }: { children: React.ReactNode }) => (
  <RequireAuth>
    <Shell>{children}</Shell>
  </RequireAuth>
);

function App() {
  return (
    <BrowserRouter>
      <MuiTheme>
        <ToastProvider>
          <AuthProvider>
            <Routes>
              <Route path="/login" element={<LoginScreen />} />
              <Route path="/" element={<Private><DashboardScreen /></Private>} />
              <Route path="/events" element={<Private><EventsScreen /></Private>} />
              <Route path="/notifications" element={<Private><NotificationsScreen /></Private>} />
              {/* Old address of the events page. */}
              <Route path="/communityEvents" element={<Navigate to="/events" replace />} />
              <Route path="*" element={<Private><NotFoundScreen /></Private>} />
            </Routes>
          </AuthProvider>
        </ToastProvider>
      </MuiTheme>
    </BrowserRouter>
  );
}

export default App;
