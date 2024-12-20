export interface ParkingLot {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  availableSpots: number;
  pricePerHour: number;
  lotType: string;
}