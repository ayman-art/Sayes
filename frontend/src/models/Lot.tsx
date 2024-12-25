export interface ParkingLot {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  availableSpots: number;
  pricePerHour: number;
  lotType: string;
}

export const jsonLotMapper = (json:any)=>{
  return{
    id: json.lot_id,
    name: `Lot ${json.lot_id}`, // You can adjust this based on how you want to name the lot
    latitude: json.latitude,
    longitude: json.longitude,
    availableSpots: json.num_of_spots,
    pricePerHour: json.price,
    lotType: json.lot_type
  }
}