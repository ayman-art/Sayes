import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L, { LatLng, Icon } from 'leaflet';
import iconUrl from '../assets/parking-sign.png';
import '../styles/DriverHomePage.css'

// Interfaces
interface ParkingLot {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  availableSpots: number;
  pricePerHour: number;
  status?: 'available' | 'full';
  amenities?: string[];
}

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

// Parking Icon
const parkingSpotIcon = new Icon({
  iconUrl: iconUrl,
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32],
});

const DriverHomePage: React.FC = () => {
  const [parkingSpots, setParkingSpots] = useState<ParkingLot[]>([
    {
      id: 1,
      name: 'Lot 1',
      latitude: 31.2,
      longitude: 29.9,
      availableSpots: 5,
      pricePerHour: 100,
      status: 'available',
    },
  ]);
  const [selectedSpot, setSelectedSpot] = useState<ParkingLot | null>(null);

  const defaultCenter: [number, number] = [30.033333, 31.233334]; // Cairo

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
                icon={parkingSpotIcon}
                eventHandlers={{
                  click: () => setSelectedSpot(spot),
                }}
              />
            ))}
          </MapContainer>
        </div>

        {/* Sidebar */}
        <div className="sidebar">
          {selectedSpot ? (
            <div className="sidebar-content">
              <h2>{selectedSpot.name}</h2>
              <p>
                <strong>Available Spots:</strong> {selectedSpot.availableSpots}
              </p>
              <p>
                <strong>Price:</strong> ${selectedSpot.pricePerHour}/hour
              </p>
              <button disabled={selectedSpot.availableSpots === 0}>
                {selectedSpot.availableSpots > 0 ? 'Book Spot' : 'No Spots Available'}
              </button>
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
