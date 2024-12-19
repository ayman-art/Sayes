import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import SignUp from "./pages/SignUp";
import Dashboard from "./pages/Dashboard"; 
import { authorizeToken, clearData, saveData } from "./services/authService";
import DriverHomePage from "./pages/DriverHome";
import LotManagerHomePage from "./pages/LotManagerHome";

const App: React.FC = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const onLogin  = ()=>{
    setIsAuthenticated(true)
  }
  const onLogout = ()=>{
    clearData()
    console.log("ay 7aga")
    setIsAuthenticated(false)
  }
  useEffect(() => {
    const check = async () => {
      const token = localStorage.getItem('jwtToken');
      console.log(token)
      if (token) {
        try {
          const data = await authorizeToken(token);
          const jwt = data['jwt'];
          localStorage.setItem('jwtToken', jwt);
          console.log("didntpass")
          saveData(token)
          console.log("passed")
          setIsAuthenticated(true); // Set to true when token is valid
        } catch (err) {
          localStorage.removeItem('jwtToken');
          setIsAuthenticated(false); // Set to false on error
          console.log(err);
        }
      } else {
        setIsAuthenticated(false); // No token, so not authenticated
      }
    };
    check();
  }, []);
  if (isAuthenticated === null) {
    return <div>Loading...</div>; // Loading screen or spinner while checking token
  }
  return (
    <Router>
      <Routes>
        {isAuthenticated ? (
          <>
            <Route path="/" element={<Dashboard onLogout={onLogout} />} />
            <Route path="/signup" element={<Navigate to="/" />}/>
            <Route path="/login" element={<Navigate to="/" />} />
          </>
        ):(
          <>
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="/login" element={<Login onLogin={onLogin} />} />
            <Route path="/signup" element={<SignUp onLogin={onLogin} />} />
          </>
        )}
        <Route path="/test" element={<LotManagerHomePage/>}/>
      </Routes>
    </Router>
  );
};

export default App;
