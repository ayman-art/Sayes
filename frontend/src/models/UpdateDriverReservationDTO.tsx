export interface UpdateDriverReservationDTO {
    driverId: number;
    lotId: number;
    spotId: number;
    status: string;
    penalty: number;
    price: number;
    remainingReservationTime: number | null;
    remainingParkingTime: number | null;
}