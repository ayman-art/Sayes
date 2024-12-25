import React, { useState, useEffect } from 'react';
//import Modal from 'react-modal';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L, { LatLng, Icon } from 'leaflet';
import redIconUrl from '../assets/red-parking-sign.png';
import blueIconUrl from '../assets/blue-parking-sign.png';
import '../styles/DriverHomePage.css';
import { jsonLotMapper, ParkingLot } from '../models/Lot';
import { fetchLots, getSpotPrice, bookSpot } from '../API/driverHomeAPI';
import WebSocketService from '../services/socketService';
import { URLS } from '../API/urls';
import { DTOLotMapper } from '../models/UpdateLotDTO';

const LocationMarker: React.FC = () => {
  const [position, setPosition] = useState<LatLng | null>(null);
  const map = useMap();

  useEffect(() => {
    map.locate().on('locationfound', (e) => {
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

const DriverHomePage: React.FC = () => {
  const [parkingSpots, setParkingSpots] = useState<ParkingLot[]>([]);
  const [selectedSpot, setSelectedSpot] = useState<ParkingLot | null>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [endTime, setEndTime] = useState<string>('');
  const [calculatedPrice, setCalculatedPrice] = useState<number | null>(null);

  const defaultCenter: [number, number] = [30.033333, 31.233334];

  const getIcon = (availableSpots: number) =>
    new Icon({
      iconUrl: availableSpots > 0 ? blueIconUrl : redIconUrl,
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
    });

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEndTime('');
    setCalculatedPrice(null);
  };

  const handleFetchPrice = async () => {
    if (!endTime || !selectedSpot) return;
    try {
      //const response = await getSpotPrice(selectedSpot.id, endTime); // Update API to accept `endTime`
      //const price = await response.json();
      //setCalculatedPrice(price);
    } catch (error) {
      setMessage('Error fetching price.');
    }
  };

  const handleConfirmBooking = async () => {
    if (!selectedSpot || !endTime) return;
    try {
      const response = await bookSpot(selectedSpot.id, endTime); // Add booking API call
      if (response.ok) {
        setMessage('Spot booked successfully!');
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
      handleCloseModal();
    } catch (error) {
      setMessage('Error booking the spot.');
    }
  };

  return (
    <div className="app-container">
      {/* Navbar */}
      <nav className="navbar">
        <div className="navbar-title">Sayes</div>
        <div className="navbar-links">
          <a href="/">Home</a>
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
                    setMessage(null);
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
              <button onClick={handleOpenModal} disabled={selectedSpot.availableSpots === 0}>
                Book Spot
              </button>
              {message && <p className="message">{message}</p>}
            </div>
          ) : (
            <div className="sidebar-empty">Select a parking spot on the map</div>
          )}
        </div>
      </div>

      {/* Modal */}
      <Modal isOpen={isModalOpen} onRequestClose={handleCloseModal} className="modal" overlayClassName="overlay">
        <h2>Book Spot</h2>
        <p>Select an end time for your reservation:</p>
        <input
          type="datetime-local"
          value={endTime}
          onChange={(e) => setEndTime(e.target.value)}
        />
        <button onClick={handleFetchPrice} disabled={!endTime}>
          Fetch Price
        </button>
        {calculatedPrice !== null && (
          <p>
            <strong>Total Price:</strong> ${calculatedPrice}
          </p>
        )}
        <button onClick={handleConfirmBooking} disabled={calculatedPrice === null}>
          Confirm Booking
        </button>
        <button onClick={handleCloseModal}>Cancel</button>
      </Modal>
    </div>
  );
};

export default DriverHomePage;
