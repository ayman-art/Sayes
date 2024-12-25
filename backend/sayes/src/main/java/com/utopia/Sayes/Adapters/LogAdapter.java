package com.utopia.Sayes.Adapters;

import com.utopia.Sayes.Models.Log;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class LogAdapter implements IAdapter<Log> {
    @Override
    public Map<String, Object> toMap(Log object) {
        Map<String, Object> map = new HashMap<>();
        map.put("driver_id", object.getDriverId());
        map.put("reservation_time", object.getReservationTime());
        map.put("departure_time", object.getDepartureTime());
        map.put("spot_id", object.getSpotId());
        map.put("lot_id", object.getLotId());
        return map;
    }

    @Override
    public Log fromMap(Map<String, Object> map) {
        Long driverId = (Long) map.get("driver_id");
        LocalDateTime reservationTime = (LocalDateTime) map.get("reservation_time");
        LocalDateTime departureTime= (LocalDateTime) map.get("departure_time");
        Long spotId = (Long) map.get("spot_id");
        Long lotId = (Long) map.get("lot_id");

        return new Log(driverId, reservationTime, departureTime, spotId, lotId);
    }
}
