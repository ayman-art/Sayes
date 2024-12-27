import React, { useState, useEffect } from 'react';
import { fetchManagerData,  fetchLots, downloadLotsReport} from '../services/ManagerDashboardService';
import '../styles/AdminProfile.css';

const ManagerDashBoard = () => {
    // States for admin data, top users, top lots, and logs
    const [managerName, setManagerName] = useState<string>('');
    const [managerRevenue, setManagerRevenue] = useState<number>(0);
    const [lots, setLots] = useState<any[]>([]);
    const [view, setView] = useState<'none' | 'lots'>('none');

    // Get the token from local storage
    const token = localStorage.getItem('jwtToken');

    // Fetch admin data on component mount
    useEffect(() => {
        const fetchData = async () => {
            try {
                const managerData = await fetchManagerData(token!);  // Ensure token is present
                setManagerName(managerData.username);
                setManagerRevenue(managerData.revenue)
                console.log(managerData.username)
            } catch (error) {
                console.error('Error fetching admin data:', error);
            }
        };

        fetchData();
    }, [token]);


    // Function to fetch top lots
    const handleLots = async () => {
        try {
            const response = await fetchLots(token!);
            setLots(response);
            setView('lots');
        } catch (error) {
            console.error('Error fetching top lots:', error);
        }
    };

      const handleDownloadLotsReport = async () => {
        if (token) {
          await  downloadLotsReport(token);  // Trigger report download
        } else {
          console.error('No token found');
        }
      };

    return (
        <div className="profile-container">
            <div className="profile-header">
                <h2 className="text-3xl font-bold">Manager Dashboard</h2>
                <div className="profile-stats">
                    <div>
                        <p>Manager</p>
                        <h3 className="text-xl font-semibold">{managerName}</h3>
                    </div>
                    <div>
                        <p>Revenue</p>
                        <h3 className="text-xl font-semibold">{managerRevenue}</h3>
                    </div>
                </div>
                <div className="profile-actions">
                    <button className="btn-secondary" onClick={handleLots}>
                        View Lots
                    </button>
                </div>
            </div>

            <div className="profile-content">
                {view === 'lots' && (
                    <div className="lots" id="lots">
                        <h3 className="text-2xl font-bold">Lots Report</h3>
                        <ul>
                            {lots.map((lot, index) => (
                                <li key={index} className="user-item">
                                    <p><strong>Lot Id:</strong> {lot.lot_id}</p>
                                    <p><strong>Revenue:</strong> {lot.revenue}</p>
                                    <p><strong>Lot Type:</strong> {lot.lot_type}</p>
                                    <p><strong>Price:</strong> {lot.price}</p>
                                    <p><strong>Available Spots:</strong> {lot.num_of_spots}</p>
                                    <p><strong>Occupancy Rate:</strong> {lot.occupancy_rate}</p>
                                </li>
                            ))}
                        </ul>
                        <button className="btn-primary" onClick={handleDownloadLotsReport}>Print Lots Report</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ManagerDashBoard;