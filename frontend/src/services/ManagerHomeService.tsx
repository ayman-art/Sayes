import { LotDetailed } from "../models/LotDetailed";
import { LotHomeLot } from "../pages/LotManagerHome";

const URL = 'http://localhost:8080';

// Define the interface for a single lot based on your response structure
interface RawLot {
  lot_id: number;
  occupancy_rate: number | string;
  revenue: number;
  lot_type: string;
  latitude: number;
  longitude: number;
  num_of_spots: number;
  price: number;
}

// Define the structure of the response from fetching lots
interface GetLotsResponse {
  lots: RawLot[];
}

// Define the structure for creating a new lot
interface CreateLotRequest {
  longitude: number;
  latitude: number;
  revenue: number;  // It should be a number, initialized to 0 when creating
  price: number;
  num_of_spots: number;
  lot_type: string;
  penalty: number;
  fee: number;
  time: string; // Duration in ISO 8601 format
}

// Define the response structure for creating a new lot
interface CreateLotResponse {
  success: boolean;
  message: string;
}

// Fetch lots for the manager
export const fetchManagerLots = async (token: string)=> {
    try {
      const response = await fetch(`${URL}/report/get-manager-lot-report`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });
  
      if (!response.ok) {
        throw new Error('Failed to fetch lots: ' + response.statusText);
      }
  
      const rawData = await response.json();
  
      // Ensure the response is structured properly
      if (!rawData?.lots || !Array.isArray(rawData.lots)) {
        throw new Error("Invalid response structure: 'lots' is missing or not an array.");
      }
  
      // Explicitly return GetLotsResponse
      return rawData['lots']
    } catch (error) {
      console.error('Error fetching lots:', error);
      throw error;
    }
  };
  

// Create a new lot
export const createNewLot = async (token: string, lotData: CreateLotRequest) => {
  try {
    const response = await fetch(`${URL}/lot-management/create-lot`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(lotData),
    });

    if (!response.ok) {
      throw new Error('Failed to create a new lot: ' + response.statusText);
    }


    return response;
  } catch (error) {
    console.error('Error creating lot:', error);
    throw error; // Re-throw the error to be handled elsewhere
  }
};

export const mapRawLot= (json: any): LotHomeLot =>{
  return {
  lot_id: json.lot_id,
  occupancy_rate: json.occupancy_rate,
  price: json.price,
  latitude: json.latitude,
  num_of_spots: json.num_of_spots,
  lot_type: json.lot_type,
  longitude: json.longitude,
  revenue: json.revenue
}
}







