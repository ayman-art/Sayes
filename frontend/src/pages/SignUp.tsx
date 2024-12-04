
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import '../styles/components.css';
import { Link } from 'react-router-dom';
import { registerDriver, registerLotManager } from "../services/authService";
import Modal from "../components/modal/Modal";
interface signupProps{
  onLogin: ()=> void
}
const SignUp: React.FC<signupProps> = ({onLogin}) => {
  const [username, setUsername] = useState<string>(""); 
  const [password, setPassword] = useState<string>("");
  const [role, setRole] = useState<"driver" | "lot-manager">("driver");
  const [plateNumber, setPlateNumber] = useState<string>("");
  const [licenseNumber, setLicenseNumber] = useState<string>("");
  const [errorMessage, setErrorMessage] = useState<string>(""); 
  const [showModal, setShowModal] = useState<boolean>(false); 
  const navigate = useNavigate();

  const validatePassword = (password: string) => {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
  };

  const validateForm = () => {
    if (!username || !password) {
      setErrorMessage("Username and Password are required.");
      setShowModal(true); // Show modal when there's an error
      return false;
    }
    if (!validatePassword(password)) {
      setErrorMessage("Password must be at least 8 characters long, and include a number and a special character.");
      setShowModal(true); // Show modal when there's an error
      return false;
    }
    if (role === "driver" && (!plateNumber || !licenseNumber)) {
      setErrorMessage("Plate number and license number are required for drivers.");
      setShowModal(true); // Show modal when there's an error
      return false;
    }
    setErrorMessage(""); // Clear error message if validation passes
    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      let response;
      if (role === "driver") {
        console.log("Registering driver...");
        response = await registerDriver(username, password, role, plateNumber, licenseNumber);
      } else {
        console.log("Registering lot manager...");
        response = await registerLotManager(username, password, role);
      }
      onLogin()
      navigate("/");
      
    } catch (error) {
      console.error("Sign up error:", error);
      setErrorMessage("Sign up failed. Please try again.");
      setShowModal(true); // Show modal on error
    }
  };

  const closeModal = () => {
    setShowModal(false); // Close modal
  };

  return (
    <div className="sign-in-container">
      <h2>Sign Up</h2>
      <form onSubmit={handleSubmit}>
        <div className="input-field">
          <label>Username</label>
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
          </div>
        </div>

        {role === "driver" && (
          <>
            <div className="input-field">
              <label>Plate Number</label>
              <input 
                type="text" 
                value={plateNumber} 
                onChange={(e) => setPlateNumber(e.target.value)} 
                required 
              />
            </div>
            <div className="input-field">
              <label>License Number</label>
              <input 
                type="text" 
                value={licenseNumber} 
                onChange={(e) => setLicenseNumber(e.target.value)} 
                required 
              />
            </div>
          </>
        )}

        <button type="submit" className="submit-button">Sign Up</button>
      </form>
      <div className="link-container">
        <p>Already have an account? <Link to="/login">Login</Link></p>
      </div>

      {/* Modal to display error */}
      {showModal && <Modal message={errorMessage} onClose={closeModal} />}
    </div>
  );
};

export default SignUp;
