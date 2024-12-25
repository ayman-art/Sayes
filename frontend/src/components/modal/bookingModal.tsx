import React, { useState } from 'react';
import Modal from 'react-modal';
import '../styles/BookingModal.css';
import { ParkingLot } from '../../models/Lot';

interface BookingModalProps {
  isOpen: boolean;
  spot: ParkingLot | null;
  onClose: () => void;
  onConfirm: (endTime: string) => Promise<void>;
  fetchPrice: (lotId: string, endTime: string) => Promise<number | null>;
}

const BookingModal: React.FC<BookingModalProps> = ({ isOpen, spot, onClose, onConfirm, fetchPrice }) => {
  const [endTime, setEndTime] = useState('');
  const [calculatedPrice, setCalculatedPrice] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleFetchPrice = async () => {
    if (spot && endTime) {
      try {
        const price = await fetchPrice(spot.id!, endTime);
        if (price !== null) {
          setCalculatedPrice(price);
          setError(null);
        } else {
          setError('Failed to fetch the price. Try again later.');
        }
      } catch {
        setError('Error fetching the price.');
      }
    }
  };

  const handleConfirm = async () => {
    if (endTime) {
      await onConfirm(endTime);
    }
  };

  return (
    <Modal isOpen={isOpen} onRequestClose={onClose} className="modal" overlayClassName="overlay">
      <h2>Confirm Booking</h2>
      {spot && (
        <div>
          <p><strong>Spot:</strong> {spot.name}</p>
          <p><strong>Available Spots:</strong> {spot.availableSpots}</p>
          <label>
            Select End Time:
            <input
              type="datetime-local"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
            />
          </label>
          <button onClick={handleFetchPrice} disabled={!endTime}>
            Fetch Price
          </button>
          {calculatedPrice !== null && <p><strong>Price:</strong> ${calculatedPrice}</p>}
          {error && <p className="error">{error}</p>}
        </div>
      )}
      <div className="modal-actions">
        <button onClick={onClose}>Cancel</button>
        <button onClick={handleConfirm} disabled={calculatedPrice === null}>
          Confirm
        </button>
      </div>
    </Modal>
  );
};

export default BookingModal;
