import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Tooltip, Popup, useMapEvents, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L, { Icon, LatLng } from 'leaflet';
import redIconUrl from '../assets/red-parking-sign.png'; // Icon for lots with 0 spots
import blueIconUrl from '../assets/blue-parking-sign.png'; // Icon for lots with available spots
import { fetchManagerLots, createNewLot, mapRawLot } from '../services/ManagerHomeService';
import WebSocketService from '../services/socketService';
import { URLS } from '../API/urls';

// Interfaces
export interface RawLot {
  lot_id: number;
  occupancy_rate: number | string;
  revenue: number;
  lot_type: string;
  latitude: number;
  longitude: number;
  num_of_spots: number;
  price: number;
  penalty: number;
  fee: number;
  time: string; // ISO 8601 duration format
}
export interface LotHomeLot {
  lot_id: number;
  occupancy_rate: number | string;
  revenue: number;
  lot_type: string;
  latitude: number;
  longitude: number;
  num_of_spots: number;
  price: number;
}

const LocationMarker: React.FC = () => {
  const [position, setPosition] = useState<LatLng | null>(null);

  const map = useMap();

  useEffect(() => {
    map.locate().on('locationfound', function (e) {
      const userLocation = e.latlng;
      setPosition(userLocation);
      map.flyTo(userLocation, map.getZoom());
    });
    
  }, [map]);

  return position === null ? null : (
    <Marker position={position}>
      <Popup>You are here</Popup>
    </Marker>
  );
};

const LotManagerHomePage: React.FC = () => {
  const [parkingSpots, setParkingSpots] = useState<LotHomeLot[]>([]); // Correctly typed as RawLot[]
  const [newLot, setNewLot] = useState<RawLot>({
    lot_id: 0,
    occupancy_rate: 0,
    revenue: 0,
    price: 0,
    num_of_spots: 0,
    lot_type: '',
    latitude: 0,
    longitude: 0,
    penalty: 10.5,
    fee: 5.5,
    time: 'PT2H', // Default time in ISO format (duration)
  });
  const [selectedPosition, setSelectedPosition] = useState<[number, number] | null>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [currentPosition, setCurrentPosition] = useState<[number, number] | null>(null);
  const webSocketService = new WebSocketService(URLS.SOCKET);
  const [notification, setNotification] = useState(null); // Notification message state

  const token = localStorage.getItem('jwtToken'); // Replace with actual token handling
  
  useEffect(() => {
    const onConnect = ()=>{
      const manager_id = localStorage.getItem('id');
      webSocketService.subscribe(`/topic/lot-manager-update/${manager_id}`, (message) => {
      handleSocket(JSON.parse(message.body));
    });
    }
    const onError = (error: string) => {
      console.error('WebSocket error:', error);
    };
    webSocketService.connect(onConnect, onError);
    if (token) {
      fetchLots();
    } else {
      setMessage('Please log in to view parking lots.');
    }
  }, [token]);
  const handleSocket = (data:any)=>{
    const status = data['status'];
    if (status == "ReservationTimeOut"){
      const msg = `Lot: ${data['lotId']}, Spot: ${data['spotId']} has had a reservation timout!`;
      showNotification(msg);
    }
    if (status == "OverOccupied"){
      const msg = `Lot: ${data['lotId']}, Spot: ${data['spotId']} has had a customer over-occupy!`;
      showNotification(msg);

    }
    if (status == "Faulty"){
      const msg = `Lot: ${data['lotId']}, Spot: ${data['spotId']} is faulty!`;
      showNotification(msg);

    }
    if (status == "Occupied"){
      const msg = `Lot: ${data['lotId']}, Spot: ${data['spotId']} is occupied!`;
      showNotification(msg);

    }
    if (status == "Available"){
      const msg = `Lot: ${data['lotId']}, Spot: ${data['spotId']} is Available!`;
      showNotification(msg);

    }
    if (status == "Reserved"){
      const msg = `Lot: ${data['lotId']}, Spot: ${data['spotId']} is Reserved!`;
      showNotification(msg);

    }
  }
  const showNotification = (message: any) => {
    setNotification(message);
    setTimeout(() => {
      setNotification(null);
    }, 5000);
  };

  const fetchLots = async () => {
    try {
      const unmappedLots: Object[]= await fetchManagerLots(token!); // Now TypeScript knows this is a GetLotsResponse
      console.log('Fetched lots raw data:', unmappedLots); // Log the raw response to check the format
      const mappedLots: LotHomeLot[] = unmappedLots.map(mapRawLot);

      setParkingSpots(mappedLots); // Access 'lots' directly
    } catch (error) {
      console.error('Error fetching lots:', error);
      setMessage('Failed to fetch parking lots.');
    }
  };

  const handleAddLot = async () => {
    if (
      newLot.latitude &&
      newLot.longitude &&
      newLot.num_of_spots &&
      newLot.price &&
      newLot.lot_type
    ) {
      const lotData = {
        longitude: newLot.longitude,
        latitude: newLot.latitude,
        revenue: newLot.revenue,
        price: newLot.price,
        num_of_spots: newLot.num_of_spots,
        lot_type: newLot.lot_type,
        penalty: newLot.penalty,
        fee: newLot.fee,
        time: newLot.time,
      };

      try {
        console.log('Sending request to create new lot:', lotData); // Log the data you're sending
        const response = await createNewLot(token!, lotData);
        console.log('Received response:', response); // Log the response

        if (response.ok) {
          setMessage('New lot added successfully!');
          
          setSelectedPosition(null); // Clear selected position
          fetchLots(); // Re-fetch the updated list of lots
        } else {
          setMessage(response.statusText || 'Failed to add lot.');
        }
      } catch (error) {
        console.error('Error creating lot:', error);
        setMessage('Failed to create a new lot.');
      }
    } else {
      setMessage('Please fill all fields and select a position on the map.');
    }
  };

  const LocationSelector: React.FC = () => {
    useMapEvents({
      click: (e) => {
        const latLng: [number, number] = [e.latlng.lat, e.latlng.lng];
        setSelectedPosition(latLng);
        setCurrentPosition(latLng)
        setNewLot((prevLot) => ({
          ...prevLot,
          latitude: e.latlng.lat,
          longitude: e.latlng.lng,
        }));
      },
    });
    return null;
  };


  const getIcon = (num_of_spots: number) =>
    new Icon({
      iconUrl: num_of_spots > 0 ? blueIconUrl : redIconUrl,
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
    });

  return (
    <div style={styles.appContainer}>
      <nav style={styles.navbar}>
        <div style={styles.navbarTitle}>Lot Manager</div>
      </nav>

      <div style={styles.content}>
        <div style={styles.mapContainer}>
          <MapContainer center={currentPosition || [30.033333, 31.233334]} zoom={13} style={styles.mapStyle} key={parkingSpots.length}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" attribution="&copy; OpenStreetMap contributors" />
            <LocationMarker />
            <LocationSelector />
            {/* <NavigateToCurrentLocation /> */}
            {parkingSpots.map((spot) => (
              <Marker
                key={spot.lot_id} // Use lot_id here
                position={[spot.latitude, spot.longitude]}
                icon={getIcon(spot.num_of_spots)}
              >
                <Tooltip>
                  <div>
                    <p><strong>{spot.lot_type}</strong></p>
                    <p>Revenue: ${spot.revenue}</p>
                    <p>Occupancy Rate: {spot.occupancy_rate}</p>
                    <p>Available Spots: {spot.num_of_spots}</p>
                    <p>Price Per Hour: ${spot.price}</p>
                  </div>
                </Tooltip>
              </Marker>
            ))}
          </MapContainer>
        </div>
        {/* Notification Component */}
        {notification && (
          <div style={styles.notification}>
            {notification}
          </div>
        )}
        <div style={styles.sidebar}>
          <h2>Add New Parking Lot</h2>
          <div style={styles.formGroup}>
            <label>Lot Name:</label>
          </div>
          <div style={styles.formGroup}>
            <label>Available Spots:</label>
            <input
              type="number"
              value={newLot.num_of_spots || ''}
              onChange={(e) => setNewLot({ ...newLot, num_of_spots: parseInt(e.target.value) })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label>Price Per Hour:</label>
            <input
              type="number"
              value={newLot.price || ''}
              onChange={(e) => setNewLot({ ...newLot, price: parseFloat(e.target.value) })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label>Latitude:</label>
            <input type="text" value={newLot.latitude || ''} readOnly style={styles.input} />
          </div>
          <div style={styles.formGroup}>
            <label>Longitude:</label>
            <input type="text" value={newLot.longitude || ''} readOnly style={styles.input} />
          </div>
          <div style={styles.formGroup}>
            <label>Lot Type:</label>
            <select
              value={newLot.lot_type || ''}
              onChange={(e) => setNewLot({ ...newLot, lot_type: e.target.value })}
              style={styles.input}
            >
              <option value="">Select Type</option>
              <option value="Regular">Regular</option>
              <option value="Disabled">Disabled</option>
              <option value="EV charging">EV charging</option>
            </select>
          </div>
          <div style={styles.formGroup}>
            <label>Penalty:</label>
            <input
              type="number"
              value={newLot.penalty || ''}
              onChange={(e) => setNewLot({ ...newLot, penalty: parseFloat(e.target.value) })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label>Fee:</label>
            <input
              type="number"
              value={newLot.fee || ''}
              onChange={(e) => setNewLot({ ...newLot, fee: parseFloat(e.target.value) })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label>Time (in minutes, e.g., 120 for 2 hours):</label>
            <input
              type="number"
              value={newLot.time ? parseInt(newLot.time.replace('PT', '').replace('H', '')) : ''}
              onChange={(e) => setNewLot({ ...newLot, time: `PT${e.target.value}M` })}
              style={styles.input}
            />
          </div>
          <button onClick={handleAddLot} style={styles.button}>
            Add Lot
          </button>
          {message && <p style={styles.message}>{message}</p>}
        </div>
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  notification: {
    position: 'fixed',
    top: '20px',
    right: '20px',
    padding: '10px 20px',
    backgroundColor: '#28a745',
    color: '#fff',
    borderRadius: '5px',
    boxShadow: '0 2px 6px rgba(0,0,0,0.2)',
    zIndex: 1000,
  },
  appContainer: { display: 'flex', flexDirection: 'column', height: '100vh' },
  navbar: { backgroundColor: '#333', color: 'white', padding: '10px', textAlign: 'center' },
  navbarTitle: { fontSize: '20px', fontWeight: 'bold' },
  content: { display: 'flex', flex: 1 },
  mapContainer: { flex: 2, position: 'relative' },
  mapStyle: { height: '100%', width: '100%' },
  sidebar: { flex: 1, padding: '20px', backgroundColor: '#f4f4f4', borderLeft: '1px solid #ddd' },
  formGroup: { marginBottom: '15px' },
  input: { width: '100%', padding: '8px', margin: '5px 0', boxSizing: 'border-box' },
  button: { padding: '10px', backgroundColor: '#28a745', color: 'white', border: 'none', cursor: 'pointer' },
  message: { marginTop: '10px', color: '#d9534f' },
};

export default LotManagerHomePage;


















