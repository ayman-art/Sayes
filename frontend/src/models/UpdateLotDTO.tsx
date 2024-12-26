export interface UpdateLotDTO {
    lotId: number;
    noOfSpots: number; 
    longitude: number;
    latitude: number;
    price:number;
    lot_type: string;
}

export const DTOLotMapper = (json:any)=>{
    return{
      id: json.lotId,
      name: `Lot ${json.lotId}`, // You can adjust this based on how you want to name the lot
      latitude: json.latitude,
      longitude: json.longitude,
      availableSpots: json.noOfSpots,
      pricePerHour: json.price,
      lotType: json.lot_type
    }
  }