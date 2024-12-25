import { AdminDataDTO } from "../models/AdminDataDTO";

const URL = 'http://localhost:8080';

// Interfaces for Top Users
interface RawTopUser {
    username: string;
    total_reservations: number;
}

interface RawTopUsersResponse {
    topUsers: RawTopUser[];
}

// Interfaces for Top Lots
interface RawTopLot {
    lot_id: number;
    revenue: number;
}

interface RawTopLotsResponse {
    topLots: RawTopLot[];
}

interface RawLog {
    driverId: number;
    reservationTime: string;
    departureTime: string;
    spotId: number;
    lotId: number;
}

interface RawLogsResponse {
    logs: RawLog[];
}

// Admin Data Fetch Function
export const fetchAdminData = async (token: string): Promise<AdminDataDTO> => {
    const response = await fetch(`${URL}/profile/get-admin`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch admin data');
    }

    const rawData = await response.json();
    console.log('Admin data:', rawData);

    const adminDataDTO: AdminDataDTO = {
        username: rawData.username,
    };

    return adminDataDTO;
};

// Top Users Fetch Function
export const fetchTopUsers = async (token: string): Promise<RawTopUsersResponse> => {
    const response = await fetch(`${URL}/report/get-top-users`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch top users');
    }

    const rawData: RawTopUsersResponse = await response.json();
    console.log('Top Users:', rawData);

    return rawData;
};

// Top Lots Fetch Function
export const fetchTopLots = async (token: string): Promise<RawTopLotsResponse> => {
    const response = await fetch(`${URL}/report/get-top-lots`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch top lots');
    }

    const rawData: RawTopLotsResponse = await response.json();
    console.log('Top Lots:', rawData);

    return rawData;
};
// Logs Fetch Function
export const fetchLogs = async (token: string): Promise<RawLogsResponse> => {
    const response = await fetch(`${URL}/report/get-logs`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch logs');
    }

    const rawData: RawLogsResponse = await response.json();
    console.log('Logs:', rawData);

    return rawData;
};


