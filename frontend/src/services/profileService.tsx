import { DriverDataDTO } from "../models/DriverDataDTO";
import { ManagerDataDTO } from "../models/ManagerDataDTO";

const URL = 'http://localhost:8080';
export const fetchManagerData = async (token: string): Promise<ManagerDataDTO> => {
    const response = await fetch(`${URL}/profile/get-manager`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    });

    if (response.ok) {
        const data = await response.json();
        console.log(data);
        const managerDataDTO = {
            username: data.manager.username,
            revenue: data.manager.revenue,
            lotsWithSpots: data.lotsWithSpots

        }
        return managerDataDTO;
    } else {
        throw new Error('Failed to fetch manager data');
    }
}

export const fetchDriverData = async (token: string): Promise<DriverDataDTO> => {
    const response = await fetch(`${URL}/profile/get-driver`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    });

    if (response.ok) {
        const data = await response.json();
        console.log(data);
        return data;
    } else {
        throw new Error('Failed to fetch driver data');
    }
}