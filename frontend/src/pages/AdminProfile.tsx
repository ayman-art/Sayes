import React, { useState, useEffect } from 'react';
import { fetchAdminData, fetchTopUsers, fetchTopLots } from '../services/adminService';
import '../styles/AdminProfile.css';

const AdminProfile = () => {
    const [adminName, setAdminName] = useState<string>('');
    const [topUsers, setTopUsers] = useState<any[]>([]);
    const [topLots, setTopLots] = useState<any[]>([]);
    const [view, setView] = useState<'none' | 'topUsers' | 'topLots'>('none');

    const token = localStorage.getItem('jwtToken');

    useEffect(() => {
        const fetchData = async () => {
            const adminData = await fetchAdminData(token!);
            setAdminName(adminData.username);
        };

        fetchData();
    }, []);

    const handleTopUsers = async () => {
        const response = await fetchTopUsers(token!);
        setTopUsers(response.topUsers);
        setView('topUsers');
    };

    const handleTopLots = async () => {
        const response = await fetchTopLots(token!);
        setTopLots(response.topLots);
        setView('topLots');
    };

    return (
        <div className="profile-container">
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
                </div>
            </div>

            <div className="profile-content">
                {view === 'topUsers' && (
                    <div className="top-users">
                        <h3 className="text-2xl font-bold">Top Users</h3>
                        <ul>
                            {topUsers.map((user, index) => (
                                <li key={index} className="user-item">
                                    <p><strong>Username:</strong> {user.username}</p>
                                    <p><strong>Total Reservations:</strong> {user.total_reservations}</p>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {view === 'topLots' && (
                    <div className="top-lots">
                        <h3 className="text-2xl font-bold">Top Lots</h3>
                        <ul>
                            {topLots.map((lot, index) => (
                                <li key={index} className="lot-item">
                                    <p><strong>Lot ID:</strong> {lot.lot_id}</p>
                                    <p><strong>Revenue:</strong> ${lot.revenue.toFixed(2)}</p>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminProfile;