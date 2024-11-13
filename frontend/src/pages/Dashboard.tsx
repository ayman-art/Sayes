import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/components.css';

const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  // Sample placeholder data
  const userRole = "driver"; // In a real app, this could come from JWT or state

  const handleLogout = () => {
    // Clear any session data or token if necessary
    // localStorage.clear();
    // Redirect to login page
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <h2>Welcome to the Dashboard</h2>
      <p>You're logged in as a {userRole}.</p>

      <div className="dashboard-content">
        <h3>Dashboard Overview</h3>
        <p>Here you can manage your tasks, view reports, and more...</p>
        
        <div className="tasks-section">
          <h4>Your Tasks</h4>
          <ul>
            <li>Task 1: Manage Parking Lots</li>
            <li>Task 2: View Driver Requests</li>
            <li>Task 3: Monitor Availability</li>
          </ul>
        </div>
        
        <button onClick={handleLogout} className="logout-button">Log Out</button>
      </div>
    </div>
  );
};

export default Dashboard;
