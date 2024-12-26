import { LotDetailed } from "./LotDetailed";

export interface ManagerDataDTO {
    revenue: number;
    username: string;
    lotsWithSpots: LotDetailed[];
}