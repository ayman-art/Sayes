import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L, { LatLng, Icon } from 'leaflet';
import redIconUrl from '../assets/red-parking-sign.png'; // For lots with 0 spots
import blueIconUrl from '../assets/blue-parking-sign.png'; // For lots with available spots
import '../styles/DriverHomePage.css';
import { jsonLotMapper, ParkingLot } from '../models/Lot';
import { fetchLots, getSpotPrice, reserveSpot } from '../API/driverHomeAPI';
import WebSocketService from '../services/socketService';
import { URLS } from '../API/urls';
import { DTOLotMapper } from '../models/UpdateLotDTO';
import Modal from "react-modal";
// Interfaces
interface ReservedSpot{
  lot_id: number;
  spot_id: number;
  endTime: string;
  occupied: boolean;
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

// Driver Home Page
const DriverHomePage: React.FC = () => {
  const [parkingSpots, setParkingSpots] = useState<ParkingLot[]>([]);
  
  const [endTime, setEndTime] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [totalPrice, setTotalPrice] = useState(0);
  const [selectedSpot, setSelectedSpot] = useState<ParkingLot | null>(null);
  const [message, setMessage] = useState<string | null>(null);
  const [reservedSpot, setReservedSpot] = useState<ReservedSpot|null>(null);
  const [hasReserved, setHasReserved] = useState<boolean> (false);

  const webSocketService = new WebSocketService(URLS.SOCKET);
  const defaultCenter: [number, number] = [30.033333, 31.233334]; // Cairo

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
  


  // Create dynamic icons for parking spots
  const getIcon = (availableSpots: number) =>
    new Icon({
      iconUrl: availableSpots > 0 ? blueIconUrl : redIconUrl,
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
    });

  // Handle booking a spot
  // const handleBooking = async () => {
  //   if (selectedSpot && selectedSpot.availableSpots > 0) {
  //     console.log(endTime)
  //     try {
  //       const response = await getSpotPrice(selectedSpot!.id!)
  //       if (response.ok) {
  //         setMessage('Spot booked successfully!');
  //         // Simulate spot availability update
  //         setParkingSpots((prevSpots) =>
  //           prevSpots.map((spot) =>
  //             spot.id === selectedSpot.id
  //               ? { ...spot, availableSpots: spot.availableSpots - 1 }
  //               : spot
  //           )
  //         );
  //       } else {
  //         setMessage('Failed to book the spot. Try again later.');
  //       }
  //     } catch (error) {
  //       setMessage('Error booking the spot.');
  //     }
  //   } else {
  //     setMessage('No available spots.');
  //   }
  // };
  const BookingHandler = async () => {
    // const price = calculateTotalPrice();
    // setTotalPrice(price);
    const response = await getSpotPrice(selectedSpot!.id, endTime);
    if(response.ok){
      const data = await response.json()
      const price = data['price']
      setTotalPrice(price); 
      setIsModalOpen(true); // Show the confirmation modal
    }
  };

  const confirmBooking = async() => {
    console.log(`Booking confirmed for ${selectedSpot!.name} until ${endTime}`);
    console.log(`Total price: $${totalPrice}`);
    const response = await reserveSpot(selectedSpot?.id!, endTime);
    const data = await response.json();
    const spot = data['spotId']
    if(response.ok){
      setReservedSpot({
        lot_id: selectedSpot?.id!,
        spot_id: spot,
        endTime: endTime,
        occupied: false
      })
      setHasReserved(true)
      setIsModalOpen(false);
      setMessage("Booking confirmed!"); // Set a success message
    }
    
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  function toggleOccupancy(): void {
    throw new Error('Function not implemented.');
  }

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
          <MapContainer center={defaultCenter} zoom={13} style={{ height: "100%", width: "100%" }}>
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
            <div className="sidebar-content">+
              {!hasReserved && (<div>
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

              {/* End Time Selection */}
              <div className="reservation-time">
                <label htmlFor="endTime">
                  <strong>End Time:</strong>
                </label>
                <input
                  type="time"
                  step="1"
                  id="endTime"
                  name="endTime"
                  value={endTime}
                  onChange={(e) => setEndTime(e.target.value)}
                />
              </div>

              <button
                onClick={BookingHandler}
                disabled={selectedSpot.availableSpots === 0 || !endTime}
              >
                Book Spot
              </button>

              {message && <p className="message">{message}</p>}
              </div>)}
              {/* Currently Reserved Spot Section */}
              {reservedSpot && (
                <div className="reserved-spot">
                  <h3>Currently Reserved Spot</h3>
                  <p>
                    <strong>Parking Lot:</strong> {reservedSpot.lot_id}
                  </p>
                  <p>
                    <strong>Parking Spot:</strong> {reservedSpot.spot_id}
                  </p>
                  <p>
                    <strong>End Time:</strong> {reservedSpot.endTime}
                  </p>
                  <p>
                    <strong>Status:</strong> {reservedSpot.occupied ? "Occupied" : "Unoccupied"}
                  </p>
                  <button onClick={toggleOccupancy}>
                    {reservedSpot.occupied ? "Unoccupy Spot" : "Occupy Spot"}
                  </button>
                </div>
              )}
            </div>
          ) : (
            <div className="sidebar-empty">Select a parking spot on the map</div>
          )}
        </div>
      </div>

      {/* Booking Confirmation Modal */}
      <Modal isOpen={isModalOpen} onRequestClose={closeModal} className="modal" overlayClassName="overlay">
        <h2>Booking Confirmation</h2>
        <p>Parking Spot: <strong>{selectedSpot?.name}</strong></p>
        <p>End Time: <strong>{endTime}</strong></p>
        <p>Total Price: <strong>${totalPrice}</strong></p>
        <button onClick={confirmBooking}>Confirm</button>
        <button onClick={closeModal}>Cancel</button>
      </Modal>
    </div>
  );
};

export default DriverHomePage;


