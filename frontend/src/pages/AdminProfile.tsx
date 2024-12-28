import React, { useState, useEffect } from 'react';
import { fetchAdminData, fetchTopUsers, fetchTopLots, fetchLogs, downloadTopUsersReport , downloadTopLotsReport , downloadViolationsReport} from '../services/adminService';
import '../styles/AdminProfile.css';

interface AdminProfileProps {
    onLogout: () => void;
}

const AdminProfile: React.FC<AdminProfileProps> = ({ onLogout }) => {
    // States for admin data, top users, top lots, and logs
    const [adminName, setAdminName] = useState<string>('');
    const [topUsers, setTopUsers] = useState<any[]>([]);
    const [topLots, setTopLots] = useState<any[]>([]);
    const [logs, setLogs] = useState<any[]>([]);
    const [view, setView] = useState<'none' | 'topUsers' | 'topLots' | 'logs'>('none');

    // Get the token from local storage
    const token = localStorage.getItem('jwtToken');

    // Fetch admin data on component mount
    useEffect(() => {
        const fetchData = async () => {
            try {
                const adminData = await fetchAdminData(token!);  // Ensure token is present
                setAdminName(adminData.username);
            } catch (error) {
                console.error('Error fetching admin data:', error);
            }
        };

        fetchData();
    }, [token]);

    // Function to fetch top users
    const handleTopUsers = async () => {
        try {
            const response = await fetchTopUsers(token!);
            setTopUsers(response.topUsers);
            setView('topUsers');
        } catch (error) {
            console.error('Error fetching top users:', error);
        }
    };

    // Function to fetch top lots
    const handleTopLots = async () => {
        try {
            const response = await fetchTopLots(token!);
            setTopLots(response.topLots);
            setView('topLots');
        } catch (error) {
            console.error('Error fetching top lots:', error);
        }
    };

    // Function to fetch logs
    const handleLogs = async () => {
        try {
            const response = await fetchLogs(token!);
            setLogs(response.logs);
            setView('logs');
        } catch (error) {
            console.error('Error fetching logs:', error);
        }
    };

    const handleDownloadTopUsersReport = async () => {
        if (token) {
          await downloadTopUsersReport(token);  // Trigger report download
        } else {
          console.error('No token found');
        }
      };

      const handleDownloadTopLotsReport = async () => {
        if (token) {
          await  downloadTopLotsReport(token);  // Trigger report download
        } else {
          console.error('No token found');
        }
      };
      const handleViolationsReport = async () => {
        if (token) {
          await downloadViolationsReport(token);  // Trigger report download
        } else {
          console.error('No token found');
        }
      };

    return (
        <div className="profile-container">
            <div className="navbar-links">
                <a href="/login" onClick={onLogout}>
                    Logout
                </a>
            </div>
            <div className="profile-header">
                <h2 className="text-3xl font-bold">Admin Profile</h2>
                <div className="profile-stats">
                    <div>
                        <h3 className="text-xl font-semibold">{adminName}</h3>
                        <p>Admin</p>
                    </div>
                </div>
                <div className="profile-actions">
                    <button className="btn-primary" onClick={handleTopUsers}>
                        View Top Users
                    </button>
                    <button className="btn-secondary" onClick={handleTopLots}>
                        View Top Lots
                    </button>
                    <button className="btn-tertiary" onClick={handleLogs}>
                        View Logs
                    </button>
                </div>
            </div>

            <div className="profile-content">
                {view === 'topUsers' && (
                    <div className="top-users" id="topUsers">
                        <h3 className="text-2xl font-bold">Top Users</h3>
                        <ul>
                            {topUsers.map((user, index) => (
                                <li key={index} className="user-item">
                                    <p><strong>Username:</strong> {user.username}</p>
                                    <p><strong>Total Reservations:</strong> {user.total_reservations}</p>
                                </li>
                            ))}
                        </ul>
                        <button className="btn-primary" onClick={handleDownloadTopUsersReport}>Print Top Users</button>
                    </div>
                )}

                {view === 'topLots' && (
                    <div className="top-lots" id="topLots">
                        <h3 className="text-2xl font-bold">Top Lots</h3>
                        <ul>
                            {topLots.map((lot, index) => (
                                <li key={index} className="lot-item">
                                    <p><strong>Lot ID:</strong> {lot.lot_id}</p>
                                    <p><strong>Revenue:</strong> ${lot.revenue.toFixed(2)}</p>
                                </li>
                            ))}
                        </ul>
                        <button className="btn-primary" onClick={handleDownloadTopLotsReport}>Print Top Lots</button>
                    </div>
                )}

                {view === 'logs' && (
                    <div className="logs" id="logs">
                        <h3 className="text-2xl font-bold">Logs</h3>
                        <ul>
                            {logs.map((log, index) => (
                                <li key={index} className="log-item">
                                    <p><strong>Driver ID:</strong> {log.driverId}</p>
                                    <p><strong>Reservation Time:</strong> {new Date(log.reservationTime).toLocaleString()}</p>
                                    <p><strong>Departure Time:</strong> {new Date(log.departureTime).toLocaleString()}</p>
                                    <p><strong>Spot ID:</strong> {log.spotId}</p>
                                    <p><strong>Lot ID:</strong> {log.lotId}</p>
                                </li>
                            ))}
                        </ul>
                        <button className="btn-primary" onClick={handleViolationsReport}>see violations report</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminProfile;


