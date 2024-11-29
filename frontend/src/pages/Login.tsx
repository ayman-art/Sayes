import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import '../styles/components.css';
import { loginUser } from "../services/authService";
import { Link } from 'react-router-dom';

const Login: React.FC = () => {
  const [username, setUsername] = useState<string>(""); 
  const [password, setPassword] = useState<string>("");
  const [role, setRole] = useState<string>("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await loginUser(username, password, role); 
      if (response.token) {
        navigate("/dashboard");
      }
    } catch (error) {
      console.error("Login error:", error);
      alert("Login failed. Please check your credentials.");
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div className="input-field">
          <label>Username</label> {/* Changed label from Email to Username */}
          <input 
            type="text" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
            required 
          />
        </div>
        <div className="input-field">
          <label>Password</label>
          <input 
            type="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            required 
          />
        </div>
        <div className="radio-group">
          <label>Choose Role:</label>
          <div className="radio-container">
            <label>
              <input 
                type="radio" 
                name="role" 
                value="driver" 
                checked={role === "driver"} 
                onChange={() => setRole("driver")} 
              />
              Driver
            </label>
            <label>
              <input 
                type="radio" 
                name="role" 
                value="lot-manager" 
                checked={role === "lot-manager"} 
                onChange={() => setRole("lot-manager")} 
              />
              Lot Manager
            </label>
            <label>
              <input 
                type="radio" 
                name="role" 
                value="admin" 
                checked={role === "admin"} 
                onChange={() => setRole("admin")} 
              />
              Admin
            </label>
          </div>
        </div>
        <button type="submit" className="submit-button">Login</button>
      </form>
      <div className="link-container">
        <p>Don't have an account? <Link to="/signup">Sign Up</Link></p>
      </div>
    </div>
  );
};

export default Login;
