package com.utopia.Sayes.Modules.DynamicPricing;

import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.PenaltyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Component
public class DynamicPricing {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    PenaltyDAO penaltyDAO;
    public Double getPrice(long lotId, Time from, Time to , long driverId) throws Exception {
        Lot lot = lotDAO.getLotById(lotId);

        LocalTime fromTime = from.toLocalTime();
        LocalTime toTime = to.toLocalTime();
        double penalty = 0;
        if (penaltyDAO.existsPenalty(driverId , lotId)) {
             penalty = penaltyDAO.getPenalty(driverId, lotId);
        }
        double ratio = priceOnDemand(lot.getNum_of_spots(), lotDAO.getLotTotalSpots(lotId)) + priceOnTime(fromTime);
        return lot.getPrice() * totalTime(fromTime, toTime) * ratio + penalty;
    }

    private double priceOnDemand(long AvailableSpots, long TotalSpots){
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

    private double priceOnTime(LocalTime from) {
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

    private double totalTime(LocalTime from, LocalTime to) {
        LocalDateTime fromDateTime = LocalDateTime.of(LocalDate.now(), from);
        LocalDateTime toDateTime = LocalDateTime.of(LocalDate.now(), to);

        if (toDateTime.isBefore(fromDateTime)) {
            toDateTime = toDateTime.plusDays(1);
        }

        long durationMillis = Duration.between(fromDateTime, toDateTime).toMillis();

        return durationMillis / 3_600_000.0;
    }

}
