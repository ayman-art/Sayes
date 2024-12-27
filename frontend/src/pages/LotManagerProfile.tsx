import React, { useState, useEffect } from 'react';
import { fetchManagerData } from '../services/profileService';
import { LotDetailed } from '../models/LotDetailed';
import WebSocketService from '../services/socketService';
import { URLS } from '../API/urls';
import '../styles/profile.css';
import { UpdateLotManagerLotSpotsDTO } from '../models/UpdateLotManagerLotSpotsDTO';
import { resolvePath } from 'react-router-dom';

const LotManagerProfile = () => {
    const [managerName, setManagerName] = useState<string>("");
    const [revenue, setRevenue] = useState<number>(0);
    const [lots, setLots] = useState<LotDetailed[]>([]);
    const [managerId, setManagerId] = useState<string>("");

    const token = localStorage.getItem('jwtToken');
    const webSocketService = new WebSocketService(URLS.SOCKET);

    useEffect(() => {
        const fetchData = async () => {
            const managerData = await fetchManagerData(token!);
            setManagerName(managerData.username);
            setLots(managerData.lotsWithSpots);
            setManagerId(localStorage.getItem('id')!);
        };

        // WebSocket connection and subscription setup
        const onConnect = () => {
            console.log('Connected to WebSocket in LotManagerProfile');

            // Subscribe to manager-specific updates
            console.log('Subscribing to:', `/topic/lot-manager-update/${managerId}`);
            console.log(localStorage.getItem('role'))
            webSocketService.subscribe(`/topic/lot-manager-update/${localStorage.getItem('id')}`, handleManagerUpdate);
        };

        const onError = (error: string) => {
            console.error('WebSocket error:', error);
        };

        fetchData();
        updateRevenue();
        webSocketService.connect(onConnect, onError);

        return () => {
            webSocketService.disconnect();
        };
    }, [managerId]); // Re-run when managerId changes


    const handleManagerUpdate = (message: any) => {
        const update = JSON.parse(message.body);
        const updateLotManagerLotSpotsDTO: UpdateLotManagerLotSpotsDTO = update;
        const spotId = updateLotManagerLotSpotsDTO.spotId;
        const lotId = updateLotManagerLotSpotsDTO.lotId;
        const newState = updateLotManagerLotSpotsDTO.status;
        const newRevenue = updateLotManagerLotSpotsDTO.lotRevenue;
        console.log("updating spot", spotId, "in lot", lotId, "to", newState);
        setLots((currentLots) =>
            currentLots.map((lot) => {
                if (lot.lot_id === lotId) {
                    return {
                        ...lot,
                        revenue: newRevenue,
                        spots: lot.spots.map((spot) =>
                            spot.spot_id === spotId ? { ...spot, state: newState } : spot
                        )
                    };
                }
                return lot;
            })
        );
        updateRevenue();
        
    };


    const updateRevenue = () => {
        let totalRevenue = 0;
        lots.forEach((lot) => {
            totalRevenue += lot.revenue;
        });
        setRevenue(totalRevenue);
    };


    return (
        <div className="profile-container">
            <div className="profile-header">
                <h2 className="text-3xl font-bold">Lot Manager Profile</h2>
                <div className="profile-stats">
                    <div>
                        <h3 className="text-xl font-semibold">{managerName}</h3>
                        <p>Manager</p>
                    </div>
                    <div>
                        <h3 className="text-xl font-semibold">${revenue.toFixed(2)}</h3>
                        <p>Total Revenue</p>
                    </div>
                    <div>
                        <h3 className="text-xl font-semibold">{lots.length}</h3>
                        <p>Total Lots</p>
                    </div>
                </div>
            </div>
            
            <div className="lots-grid">
                {lots.map((lot) => (
                    <div key={lot.lot_id} className="lot-card">
                        <div className="lot-header">
                            <h4 className="text-xl font-semibold">Lot #{lot.lot_id}</h4>
                            <p className="text-sm opacity-75">{lot.lot_type}</p>
                        </div>
                        
                        <div className="lot-body">
                            <div className="lot-info">
                                <div className="info-grid">
                                    <div className="info-item">
                                        <p><strong>Price:</strong> ${lot.price.toFixed(2)}</p>
                                        <p><strong>Revenue:</strong> ${lot.revenue.toFixed(2)}</p>
                                    </div>
                                    <div className="info-item">
                                        <p><strong>Penalty:</strong> ${lot.penalty.toFixed(2)}</p>
                                        <p><strong>Fee:</strong> ${lot.fee.toFixed(2)}</p>
                                    </div>
                                    <div className="info-item">
                                        <p><strong>Location:</strong> {lot.latitude.toFixed(6)}, {lot.longitude.toFixed(6)}</p>
                                    </div>
                                </div>
                                {lot.details && (
                                    <div className="details-section">
                                        <p><strong>Details:</strong> {lot.details}</p>
                                    </div>
                                )}
                            </div>
                            
                            <h5 className="text-lg font-semibold mb-2">Parking Spots ({lot.spots.length})</h5>
                            <div className="spots-grid">
                                {lot.spots.map((spot) => (
                                    <div
                                        key={spot.spot_id}
                                        className="spot-item"
                                    >
                                        <div className="spot-id">#{spot.spot_id}</div>
                                        <div className="spot-status">{spot.state}</div>
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