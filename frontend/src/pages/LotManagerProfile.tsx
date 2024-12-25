import React, { useState, useEffect } from 'react';
import { fetchDriverData, fetchManagerData } from '../services/profileService';
import { LotDetailed } from '../models/LotDetailed';

const LotManagerProfile: React.FC = () => {

    const [managerName, setManagerName] = useState<string>("");
    const [revenue, setRevenue] = useState<number>(0);
    const [lots, setLots] = useState<LotDetailed[]>([]);

    const token = localStorage.getItem('jwtToken');

    useEffect(() => {
        const fetchData = async () => {
            const managerData = await fetchManagerData(token!);
            console.log(managerData);
            setManagerName(managerData.username);
            setRevenue(managerData.revenue);
            setLots(managerData.lotsWithSpots);
        };

        fetchData();
    }, []);

    return (
        <div>
            <h2>Lot Manager Profile</h2>
            <div>
                <p><strong>Name:</strong> {managerName}</p>
                <p><strong>Revenue:</strong> ${revenue.toFixed(2)}</p>
                <h3>Lots</h3>
                
            </div>
        </div>
    );
};

export default LotManagerProfile;
