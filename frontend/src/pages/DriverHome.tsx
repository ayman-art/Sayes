import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L, { LatLng, Icon } from 'leaflet';
import redIconUrl from '../assets/red-parking-sign.png'; // For lots with 0 spots
import blueIconUrl from '../assets/blue-parking-sign.png'; // For lots with available spots
import '../styles/DriverHomePage.css';
import { jsonLotMapper, ParkingLot } from '../models/Lot';
import { fetchLots } from '../API/driverHomeAPI';
import WebSocketService from '../services/socketService';
import { URLS } from '../API/urls';
import { DTOLotMapper } from '../models/UpdateLotDTO';

// Interfaces


// Location Marker Component
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

// Driver Home Page
const DriverHomePage: React.FC = () => {
  const [parkingSpots, setParkingSpots] = useState<ParkingLot[]>([]);
  const lotUpdateHandler = (lotUpdate: any)=>{
    setParkingSpots((prevSpots) => {
      // Check if the lot already exists in the list
      const index = prevSpots.findIndex((spot) => spot.id === lotUpdate.lotId);
  
      if (index !== -1) {
        // If found, replace the existing lot with the updated one
        const updatedSpots = [...prevSpots];
        updatedSpots[index] = DTOLotMapper(lotUpdate);
        return updatedSpots;
      } else {
        // If not found, add the new lot to the list
        return [...prevSpots, DTOLotMapper(lotUpdate)];
      }
    });
  }
  const webSocketService = new WebSocketService(URLS.SOCKET);
  useEffect(()=>{
    const onConnect = () => {
      console.log('Connected to WebSocket');

      // Subscribe to lot updates
      webSocketService.subscribe('/topic/lots-update', (message) => {
        console.log(JSON.parse(message.body));
        const lotUpdate = JSON.parse(message.body)
        lotUpdateHandler(lotUpdate)
      });
    }
    const onError = (error: string) => {
      console.error('WebSocket error:', error);
    };
    webSocketService.connect(onConnect, onError);
    const makeRequests = async()=>{
      const jwt= localStorage.getItem('jwtToken');

      const lots = await fetchLots(jwt!);
      const update: ParkingLot[] = lots.map(jsonLotMapper);
      setParkingSpots(update)
    }
    makeRequests()
    return () => {
      webSocketService.disconnect();
    };
  }, [])
  //   {
  //     id: 1,
  //     name: 'Lot 1',
  //     latitude: 31.2,
  //     longitude: 29.9,                          
  //     availableSpots: 0,
  //     pricePerHour: 100,
  //     lotType: 'Normal',
  //   },
  //   {
  //     id: 2,
  //     name: 'Lot 2',
  //     latitude: 31.3,
  //     longitude: 29.8,
  //     availableSpots: 5,
  //     pricePerHour: 80,
  //     lotType: 'VIP',
  //   },
  // ]);
  const [selectedSpot, setSelectedSpot] = useState<ParkingLot | null>(null);
  const [message, setMessage] = useState<string | null>(null);

  const defaultCenter: [number, number] = [30.033333, 31.233334]; // Cairo

  // Create dynamic icons for parking spots
  const getIcon = (availableSpots: number) =>
    new Icon({
      iconUrl: availableSpots > 0 ? blueIconUrl : redIconUrl,
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
    });

  // Handle booking a spot
  const handleBooking = async () => {
    if (selectedSpot && selectedSpot.availableSpots > 0) {
      try {
        const response = await fetch('/api/book-spot', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ spotId: selectedSpot.id }),
        });
        if (response.ok) {
          setMessage('Spot booked successfully!');
          // Simulate spot availability update
          setParkingSpots((prevSpots) =>
            prevSpots.map((spot) =>
              spot.id === selectedSpot.id
                ? { ...spot, availableSpots: spot.availableSpots - 1 }
                : spot
            )
          );
        } else {
          setMessage('Failed to book the spot. Try again later.');
        }
      } catch (error) {
        setMessage('Error booking the spot.');
      }
    } else {
      setMessage('No available spots.');
    }
  };

  return (
    <div className="app-container">
      {/* Navbar */}
      <nav className="navbar">
        <div className="navbar-title">Sayes</div>
        <div className="navbar-links">
          <a href="#">Home</a>
          <a href="#">Reservations</a>
          <a href="#">Account</a>
        </div>
      </nav>

      {/* Main Content */}
      <div className="content">
        {/* Map Section */}
        <div className="map-container">
          <MapContainer center={defaultCenter} zoom={13} style={{ height: '100%', width: '100%' }}>
            <TileLayer
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution="&copy; OpenStreetMap contributors"
            />
            <LocationMarker />
            {parkingSpots.map((spot) => (
              <Marker
                key={spot.id}
                position={[spot.latitude, spot.longitude]}
                icon={getIcon(spot.availableSpots)}
                eventHandlers={{
                  click: () => {
                    setSelectedSpot(spot);
                    setMessage(null); // Clear any previous message
                  },
                }}
              >
                <Popup>
                  <strong>{spot.name}</strong>
                  <br />
                  <strong>Type:</strong> {spot.lotType}
                  <br />
                  <strong>Available Spots:</strong> {spot.availableSpots}
                  <br />
                  <strong>Price:</strong> ${spot.pricePerHour}/hour
                </Popup>
              </Marker>
            ))}
          </MapContainer>
        </div>

        {/* Sidebar */}
        <div className="sidebar">
          {selectedSpot ? (
            <div className="sidebar-content">
              <h2>{selectedSpot.name}</h2>
              <p>
                <strong>Lot Type:</strong> {selectedSpot.lotType}
              </p>
              <p>
                <strong>Available Spots:</strong> {selectedSpot.availableSpots}
              </p>
              <p>
                <strong>Price:</strong> ${selectedSpot.pricePerHour}/hour
              </p>
              <button onClick={handleBooking} disabled={selectedSpot.availableSpots === 0}>
                Book Spot
              </button>
              {message && <p className="message">{message}</p>}
            </div>
          ) : (
            <div className="sidebar-empty">Select a parking spot on the map</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DriverHomePage;
