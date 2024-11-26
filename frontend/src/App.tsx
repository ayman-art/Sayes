import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import SignUp from "./pages/SignUp";
import Dashboard from "./pages/Dashboard"; 
import { getToken } from "./services/authService";

const App: React.FC = () => {
  const isAuthenticated = getToken() !== null;

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route 
          path="/dashboard" 
          element={isAuthenticated ? <Dashboard /> : <Login />} 
        />
      </Routes>
    </Router>
  );
};

export default App;
