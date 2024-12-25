import { Spot } from "./Spot";

export interface LotDetailed {
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
    spots: Spot[];
}
