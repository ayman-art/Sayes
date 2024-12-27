import { ManagerDataDTO } from "../models/ManagerDataDTO";
import { saveAs } from 'file-saver';

const URL = 'http://localhost:8080';
// Interfaces for Lots
interface RawLot {
    lot_id: number;
    revenue: number;
    lot_type: String;
    price: number;
    num_of_spots: number;
    occupancy_rate: number;
}

interface RawLotsResponse {
    topLots: RawLot[];
}
// Admin Data Fetch Function
export const fetchManagerData = async (token: string): Promise<ManagerDataDTO> => {
    const response = await fetch(`${URL}/profile/get-manager`, {
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
    console.log('Manager data:', rawData);

    return rawData['manager'];
};
// Top Lots Fetch Function
export const fetchLots = async (token: string) => {
    const response = await fetch(`${URL}/report/get-manager-lot-report`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error('Failed to fetch lots');
    }

    const rawData = await response.json()
    return rawData['lots'];
};
export const downloadLotsReport = async (token: string) => {
    try {
      const response = await fetch(`${URL}/report/generate-manager-report`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
  
      if (!response.ok) {
        throw new Error('Failed to fetch the report: ' + response.statusText);
      }
  
      // Get the PDF data from the response
      const blob = await response.blob();
  
      // Save the PDF file
      saveAs(blob, 'lots_report.pdf');
    } catch (error) {
      console.error('Error downloading the report:', error);
    }
  };