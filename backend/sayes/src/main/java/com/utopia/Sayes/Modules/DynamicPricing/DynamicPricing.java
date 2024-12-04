package com.utopia.Sayes.Modules.DynamicPricing;

import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Repo.LotDAO;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;

public class DynamicPricing {
    public static Double getPrice(long lotId, Time from, Time to) throws Exception {
        LotDAO lotDAO = new LotDAO();
        Lot lot = lotDAO.getLotById(lotId);

        LocalTime fromTime = from.toLocalTime();
        LocalTime toTime = to.toLocalTime();

        double ratio = priceOnDemand(lot.getNum_of_spots(), lotDAO.getLotTotalSpots(lotId)) + priceOnTime(fromTime);
        return lot.getPrice() * totalTime(fromTime, toTime) * ratio;
    }

    private static double priceOnDemand(long AvailableSpots, long TotalSpots){
        double ratio = (double) AvailableSpots / TotalSpots;
        if(ratio < 0.2){
            return 0.5;
        }
        else if(ratio < 0.5){
            return 0.2;
        }
        else if(ratio < 0.8){
            return 0.1;
        }
        else{
            return 0.0;
        }
    }

    private static double priceOnTime(LocalTime from) {
        int hour = from.getHour();
        if (hour >= 10 && hour <= 16) {
            return 0.35;
        } else if (hour >= 17 && hour <= 20) {
            return 0.25;
        } else if (hour >= 21) {
            return 0.15;
        } else {
            return 0.1;
        }
    }

    private static double totalTime(LocalTime from, LocalTime to) {
        return Duration.between(from, to).toMillis() / 3_600_000.0;
    }

}
