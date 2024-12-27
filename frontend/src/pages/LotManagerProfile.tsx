import React, { useState, useEffect } from 'react';
import { addSpots, fetchManagerData } from '../services/profileService';
import { LotDetailed } from '../models/LotDetailed';
import WebSocketService from '../services/socketService';
import { URLS } from '../API/urls';
import '../styles/profile.css';
import { UpdateLotManagerLotSpotsDTO } from '../models/UpdateLotManagerLotSpotsDTO';
import TopNav from '../components/navbar';
import { Spot } from '../models/Spot';

interface LotManagerProfileProps {
    onLogout: () => void;
}

const LotManagerProfile: React.FC<LotManagerProfileProps> = ({ onLogout }) => {
    const [managerName, setManagerName] = useState<string>("");
    const [revenue, setRevenue] = useState<number>(0);
    const [lots, setLots] = useState<LotDetailed[]>([]);
    const [managerId, setManagerId] = useState<string>("");
    const [showPopup, setShowPopup] = useState<boolean>(false);
    const [selectedLotId, setSelectedLotId] = useState<number | null>(null);
    const [spotsToAdd, setSpotsToAdd] = useState<number>(0);

    const token = localStorage.getItem('jwtToken');
    const webSocketService = new WebSocketService(URLS.SOCKET);

    useEffect(() => {
        const fetchData = async () => {
            const managerData = await fetchManagerData(token!);
            setManagerName(managerData.username);
            setLots(managerData.lotsWithSpots);
            setManagerId(localStorage.getItem('id')!);
        };

        const onConnect = () => {
            console.log('Connected to WebSocket in LotManagerProfile');
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
    }, [managerId]);

    const handleManagerUpdate = (message: any) => {
        const update = JSON.parse(message.body);
        const updateLotManagerLotSpotsDTO: UpdateLotManagerLotSpotsDTO = update;
        const spotId = updateLotManagerLotSpotsDTO.spotId;
        const lotId = updateLotManagerLotSpotsDTO.lotId;
        const newState = updateLotManagerLotSpotsDTO.status;
        const newRevenue = updateLotManagerLotSpotsDTO.lotRevenue;
        const spotIdsBatch = updateLotManagerLotSpotsDTO.spotIdsBatch;
        console.log("updating spot:", spotId, "in lot:", lotId, "to state:", newState, "new revenue:", newRevenue, "spotIdsBatch:", spotIdsBatch);
        setLots((currentLots) =>
            currentLots.map((lot) => {
                if (lot.lot_id === lotId) {
                    if (spotId === -1) {
                        // Add new spots to the lot
                        const newSpots: Spot[] = spotIdsBatch.map((id) => ({
                            spot_id: id,
                            lot_id: lotId,
                            state: newState,
                        }));
        
                        return {
                            ...lot,
                            spots: [...lot.spots, ...newSpots],
                        };
                    } else {
                        return {
                            ...lot,
                            revenue: newRevenue,
                            spots: lot.spots.map((spot) =>
                                spot.spot_id === spotId
                                    ? { ...spot, state: newState }
                                    : spot
                            ),
                        };
                    }
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

    const handleAddSpots = async () => {
        if (selectedLotId !== null) {
            await addSpots(token!, selectedLotId, spotsToAdd);
            console.log(`Lot ID: ${selectedLotId}, Spots to add: ${spotsToAdd}`);
            setShowPopup(false);
            setSpotsToAdd(0);
            updateRevenue();
        }
    };

    return (
        <>
        
        <div className="profile-container">
        <div className="navbar-links">
          <a href="/">Home</a>
          <a href="/login" onClick={onLogout}>
            Logout
          </a>
        </div>
            <div className="profile-header">
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
                            <button
                                className="add-spots-button"
                                onClick={() => {
                                    setSelectedLotId(lot.lot_id);
                                    setShowPopup(true);
                                }}
                            >
                                Add Spots
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            {showPopup && (
                <div className="popup">
                    <div className="popup-content">
                        <h3 className="text-xl font-bold">Add Spots</h3>
                        <input
                            type="number"
                            value={spotsToAdd}
                            onChange={(e) => setSpotsToAdd(Number(e.target.value))}
                            placeholder="Enter number of spots"
                            className="spots-input"
                        />
                        <div className="popup-actions">
                            <button className="submit-button" onClick={handleAddSpots}>Submit</button>
                            <button className="cancel-button" onClick={() => setShowPopup(false)}>Cancel</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
        </>
    );
};

export default LotManagerProfile;