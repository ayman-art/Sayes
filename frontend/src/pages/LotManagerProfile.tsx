import React, { useState, useEffect } from 'react';
import { fetchManagerData } from '../services/profileService';
import { LotDetailed } from '../models/LotDetailed';

const LotManagerProfile = () => {
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
        <div className="p-6 max-w-7xl mx-auto">
            <h2 className="text-3xl font-bold mb-6">Lot Manager Profile</h2>
            <div className="bg-white rounded-lg shadow-md p-6 mb-8">
                <p className="text-lg mb-2"><strong>Name:</strong> {managerName}</p>
                <p className="text-lg mb-4"><strong>Revenue:</strong> ${revenue.toFixed(2)}</p>
            </div>
            
            <h3 className="text-2xl font-semibold mb-4">Managed Lots</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {lots.map((lot) => (
                    <div key={lot.lot_id} className="bg-white rounded-lg shadow-md p-6">
                        <div className="mb-4">
                            <h4 className="text-xl font-semibold mb-2">Lot #{lot.lot_id}</h4>
                            <p className="mb-1"><strong>Type:</strong> {lot.lot_type}</p>
                            <p className="mb-1"><strong>Price:</strong> ${lot.price.toFixed(2)}</p>
                            <p className="mb-1"><strong>Revenue:</strong> ${lot.revenue.toFixed(2)}</p>
                            <p className="mb-1"><strong>Location:</strong> {lot.latitude.toFixed(6)}, {lot.longitude.toFixed(6)}</p>
                            {lot.details && <p className="mb-1"><strong>Details:</strong> {lot.details}</p>}
                        </div>
                        
                        <div>
                            <h5 className="text-lg font-semibold mb-2">Spots ({lot.spots.length})</h5>
                            <div className="grid grid-cols-5 gap-2">
                                {lot.spots.map((spot) => (
                                    <div
                                        key={spot.spot_id}
                                        className={`p-2 rounded-md text-center text-sm ${
                                            spot.state === 'Available' 
                                                ? 'bg-green-100 text-green-800'
                                                : 'bg-yellow-100 text-yellow-800'
                                        }`}
                                    >
                                        #{spot.spot_id}
                                        <div className="text-xs">{spot.state}</div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default LotManagerProfile;