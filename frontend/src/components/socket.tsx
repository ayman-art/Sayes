import React, { useEffect } from "react";
import { Client, StompSubscription } from "@stomp/stompjs";
import { UpdateDriverReservationDTO } from "../models/UpdateDriverReservationDTO";
import { UpdateLotDTO } from "../models/UpdateLotDTO";
import { UpdateLotManagerLotSpotsDTO } from "../models/UpdateLotManagerLotSpotsDTO";
import SockJS from "sockjs-client";

interface WebSocketHandlerProps {
    onLotUpdate?: (lotUpdate: UpdateLotDTO) => void;
    onLotManagerUpdate?: (managerUpdate: UpdateLotManagerLotSpotsDTO) => void;
    onDriverReservationUpdate?: (reservationUpdate: UpdateDriverReservationDTO) => void;
    lotId: number; 
    driverId: number;
}

const WebSocketHandler: React.FC<WebSocketHandlerProps> = ({
    onLotUpdate,
    onLotManagerUpdate,
    onDriverReservationUpdate,
    lotId,
    driverId,
}) => {
    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
            reconnectDelay: 5000,
            debug: (str) => console.log(str),
        });

        let lotUpdateSub: StompSubscription | null = null;
        let managerUpdateSub: StompSubscription | null = null;
        let driverReservationSub: StompSubscription | null = null;

        client.onConnect = () => {
            console.log("Connected to WebSocket");

            lotUpdateSub = client.subscribe("/topic/lots-update", (message) => {
                const data: UpdateLotDTO = JSON.parse(message.body);
                console.log("Lot Update:", data);
                if (onLotUpdate) onLotUpdate(data);
            });

            managerUpdateSub = client.subscribe(`/topic/lot-manager-update/${lotId}`, (message) => {
                const data: UpdateLotManagerLotSpotsDTO = JSON.parse(message.body);
                console.log(`Lot Manager Update for Lot ${lotId}:`, data);
                if (onLotManagerUpdate) onLotManagerUpdate(data);
            });

            driverReservationSub = client.subscribe(`/topic/driver-reservation-update/${driverId}`, (message) => {
                const data: UpdateDriverReservationDTO = JSON.parse(message.body);
                console.log(`Driver Reservation Update for Driver ${driverId}:`, data);
                if (onDriverReservationUpdate) onDriverReservationUpdate(data);
            });
        };

        client.onDisconnect = () => {
            console.log("Disconnected from WebSocket");
        };

        client.activate();

        return () => {
            lotUpdateSub?.unsubscribe();
            managerUpdateSub?.unsubscribe();
            driverReservationSub?.unsubscribe();
            client.deactivate();
        };
    }, [lotId, driverId, onLotUpdate, onLotManagerUpdate, onDriverReservationUpdate]);

    return null;
};

export default WebSocketHandler;