import { DriverDataDTO } from "../models/DriverDataDTO";
import { ManagerDataDTO } from "../models/ManagerDataDTO";
import { LotDetailed } from "../models/LotDetailed";
import { Spot } from "../models/Spot";

const URL = 'http://localhost:8080';

interface RawLotWithSpots {
    lot: {
        lot_id: number;
        manager_id: number;
        longitude: number;
        latitude: number;
        revenue: number;
        price: number;
        num_of_spots: number;
        lot_type: string;
        penalty: number;
        fee: number;
        time: string;
        details?: string;
    };
    spots: Spot[];
}

interface RawManagerResponse {
    lotsWithSpots: RawLotWithSpots[];
    manager: {
        revenue: number;
        username: string;
    };
}

export const fetchManagerData = async (token: string): Promise<ManagerDataDTO> => {
    const response = await fetch(`${URL}/profile/get-manager`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch manager data');
    }

    const rawData: RawManagerResponse = await response.json();
    console.log('Raw response:', rawData);

    const transformedLots: LotDetailed[] = rawData.lotsWithSpots.map(({ lot, spots }) => ({
        ...lot,
        spots
    }));

    const managerDataDTO: ManagerDataDTO = {
        username: rawData.manager.username,
        revenue: rawData.manager.revenue,
        lotsWithSpots: transformedLots
    };

    console.log('Transformed data:', managerDataDTO);
    return managerDataDTO;
};

export const fetchDriverData = async (token: string): Promise<DriverDataDTO> => {
    const response = await fetch(`${URL}/profile/get-driver`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch driver data');
    }

    const data = await response.json();
    console.log('Driver data:', data);
    return data;
};

// Function to add an amount for the driver
export const addAmount = async (token: string, amount: number): Promise<string> => {
    const amountData = {
        amount: amount
    }
    const response = await fetch(`${URL}/profile/add-amount`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        
        body: JSON.stringify(amountData)
    });

    if (!response.ok) {
        throw new Error('Failed to add amount');
    }

    const result = await response.text();  // Assuming the response is a plain text message
    console.log('Response:', result);
    return result;
};
