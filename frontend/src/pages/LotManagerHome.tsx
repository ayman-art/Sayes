import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, useMapEvents, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L, { Icon } from 'leaflet';
import redIconUrl from '../assets/red-parking-sign.png'; // For lots with 0 spots
import blueIconUrl from '../assets/blue-parking-sign.png'; // For lots with available spots
// Interfaces
interface ParkingLot {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  availableSpots: number;
  pricePerHour: number;
  lotType: string;
}

// Lot Manager Home Page
const LotManagerHomePage: React.FC = () => {
  const [parkingSpots, setParkingSpots] = useState<ParkingLot[]>([]);
  const [newLot, setNewLot] = useState<Partial<ParkingLot>>({});
  const [selectedPosition, setSelectedPosition] = useState<[number, number] | null>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [currentPosition, setCurrentPosition] = useState<[number, number] | null>(null);

  useEffect(() => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setCurrentPosition([position.coords.latitude, position.coords.longitude]);
      },
      (error) => {
        console.error('Error getting location:', error);
      }
    );
  }, []);

  // Handle map click to select position
  const LocationSelector: React.FC = () => {
    useMapEvents({
      click: (e) => {
        setSelectedPosition([e.latlng.lat, e.latlng.lng]);
      },
    });
    return null;
  };

  // Automatically navigate to current location
  const NavigateToCurrentLocation: React.FC = () => {
    const map = useMap();
    useEffect(() => {
      if (currentPosition) {
        map.setView(currentPosition, 13);
      }
    }, [currentPosition, map]);
    return null;
  };

  // Add a new lot
  const handleAddLot = () => {
    if (selectedPosition && newLot.name && newLot.availableSpots && newLot.pricePerHour && newLot.lotType) {
      const newLotData: ParkingLot = {
        id: parkingSpots.length + 1,
        name: newLot.name,
        latitude: selectedPosition[0],
        longitude: selectedPosition[1],
        availableSpots: newLot.availableSpots,
        pricePerHour: newLot.pricePerHour,
        lotType: newLot.lotType,
      };
      setParkingSpots([...parkingSpots, newLotData]);
      setMessage('New lot added successfully!');
      setNewLot({});
      setSelectedPosition(null);
    } else {
      setMessage('Please fill all fields and select a position on the map.');
    }
  };
  const getIcon = (availableSpots: number) =>
    new Icon({
      iconUrl: availableSpots > 0 ? blueIconUrl : redIconUrl,
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
    });

  return (
    <div style={styles.appContainer}>
      {/* Navbar */}
      <nav style={styles.navbar}>
        <div style={styles.navbarTitle}>Lot Manager</div>
      </nav>

      {/* Main Content */}
      <div style={styles.content}>
        {/* Map Section */}
        <div style={styles.mapContainer}>
          <MapContainer
            center={currentPosition || [30.033333, 31.233334]}
            zoom={13}
            style={styles.mapStyle}
          >
            <TileLayer
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution="&copy; OpenStreetMap contributors"
            />
            <LocationSelector />
            <NavigateToCurrentLocation />
            {parkingSpots.map((spot) => (
              <Marker
                key={spot.id}
                position={[spot.latitude, spot.longitude]}
                icon={getIcon(spot.availableSpots)}
              />
            ))}
          </MapContainer>
        </div>

        {/* Sidebar */}
        <div style={styles.sidebar}>
          <h2>Add New Parking Lot</h2>
          <div style={styles.formGroup}>
            <label>Lot Name:</label>
            <input
              type="text"
              value={newLot.name || ''}
              onChange={(e) => setNewLot({ ...newLot, name: e.target.value })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label>Available Spots:</label>
            <input
              type="number"
              value={newLot.availableSpots || ''}
              onChange={(e) => setNewLot({ ...newLot, availableSpots: parseInt(e.target.value) })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label>Price Per Hour:</label>
            <input
              type="number"
              value={newLot.pricePerHour || ''}
              onChange={(e) => setNewLot({ ...newLot, pricePerHour: parseFloat(e.target.value) })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label>Lot Type:</label>
            <select
              value={newLot.lotType || ''}
              onChange={(e) => setNewLot({ ...newLot, lotType: e.target.value })}
              style={styles.input}
            >
              <option value="">Select Type</option>
              <option value="Normal">Normal</option>
              <option value="VIP">VIP</option>
            </select>
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

// Inline CSS Styles
const styles: { [key: string]: React.CSSProperties } = {
  appContainer: {
    fontFamily: 'Arial, sans-serif',
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
  },
  navbar: {
    backgroundColor: '#333',
    color: 'white',
    padding: '10px',
    textAlign: 'center',
  },
  navbarTitle: {
    fontSize: '20px',
    fontWeight: 'bold',
  },
  content: {
    display: 'flex',
    flex: 1,
  },
  mapContainer: {
    flex: 2,
    position: 'relative',
  },
  mapStyle: {
    height: '100%',
    width: '100%',
  },
  sidebar: {
    flex: 1,
    padding: '20px',
    backgroundColor: '#f4f4f4',
    borderLeft: '1px solid #ddd',
  },
  formGroup: {
    marginBottom: '15px',
  },
  input: {
    width: '100%',
    padding: '8px',
    margin: '5px 0',
    boxSizing: 'border-box',
  },
  button: {
    padding: '10px',
    backgroundColor: '#28a745',
    color: 'white',
    border: 'none',
    cursor: 'pointer',
  },
  message: {
    marginTop: '10px',
    color: '#d9534f',
  },
};

export default LotManagerHomePage;
