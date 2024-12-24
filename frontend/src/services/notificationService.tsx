import React, { useEffect, useState } from 'react';
import WebSocketService from './socketService';
import { URLS } from '../API/urls';

const webSocketUrl = URLS.SOCKET; // Replace with your WebSocket endpoint

const NotificationListener: React.FC = () => {
  const [lotUpdates, setLotUpdates] = useState<any[]>([]);
  const [lotManagerUpdates, setLotManagerUpdates] = useState<any[]>([]);
  const [driverReservations, setDriverReservations] = useState<any[]>([]);

  const webSocketService = new WebSocketService(webSocketUrl);

  useEffect(() => {
    const onConnect = () => {
      console.log('Connected to WebSocket');

      // Subscribe to lot updates
      webSocketService.subscribe('/topic/lots-update', (message) => {
        console.log(message);
      });

      // // Subscribe to lot manager updates
      // webSocketService.subscribe('/topic/lot-manager-update/123', (message) => {
      //   setLotManagerUpdates((prev) => [...prev, JSON.parse(message.body)]);
      // });

      // // Subscribe to driver reservations
      // webSocketService.subscribe('/topic/driver-reservation-update/456', (message) => {
      //   setDriverReservations((prev) => [...prev, JSON.parse(message.body)]);
      // });
    };

    const onError = (error: string) => {
      console.error('WebSocket error:', error);
    };

    webSocketService.connect(onConnect, onError);

    return () => {
      webSocketService.disconnect();
    };
  }, []);

  return (
    <div>
      <h2>Lot Updates</h2>
      <ul>
        {lotUpdates.map((update, index) => (
          <li key={index}>{JSON.stringify(update)}</li>
        ))}
      </ul>

      <h2>Lot Manager Updates</h2>
      <ul>
        {lotManagerUpdates.map((update, index) => (
          <li key={index}>{JSON.stringify(update)}</li>
        ))}
      </ul>

      <h2>Driver Reservations</h2>
      <ul>
        {driverReservations.map((update, index) => (
          <li key={index}>{JSON.stringify(update)}</li>
        ))}
      </ul>
    </div>
  );
};

export default NotificationListener;
