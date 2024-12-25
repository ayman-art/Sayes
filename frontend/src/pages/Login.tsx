import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import '../styles/components.css';
import { loginUser, saveData } from "../services/authService";
import { Link } from 'react-router-dom';
interface loginProps{
  onLogin: ()=> void
}
const Login: React.FC<loginProps> = ({onLogin}) => {
  const [username, setUsername] = useState<string>(""); 
  const [password, setPassword] = useState<string>("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await loginUser(username, password);
      const token = localStorage.getItem('jwtToken')
      saveData(token!) 
      onLogin()
      navigate("/");
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
        
        <button type="submit" className="submit-button">Login</button>
      </form>
      <div className="link-container">
        <p>Don't have an account? <Link to="/signup">Sign Up</Link></p>
      </div>
    </div>
  );
};

export default Login;
