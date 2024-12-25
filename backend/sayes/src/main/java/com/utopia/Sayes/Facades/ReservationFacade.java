package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.ReservationService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReservationFacade {
    @Autowired
    ReservationService reservationService;

    public Map<String, Object> reserveSpot(Map<String , Object> spotData) throws Exception {
        try {
            String jwt = (String) spotData.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long driverId = Long.parseLong(claims.getId());
            if (driverId == null)
                throw new Exception("driver id is null");
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            java.util.Date date = format.parse((String) spotData.get("endTime"));
            java.sql.Time sqlTime = new java.sql.Time(date.getTime());
            long spotId = reservationService.reserveSpot(Long.valueOf((Integer)  spotData.get("lotId")) , driverId ,sqlTime);
            Map<String , Object> data = new HashMap<>();
            data.put("spotId" , spotId);
            return data;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public void useSpot(Map<String , Object> spotData) throws Exception {
        try {
            String jwt = (String) spotData.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long driverId = Long.parseLong(claims.getId());
            if (driverId == null)
                throw new Exception("driver id is null");
            reservationService.useReservation(Long.valueOf((Integer)  spotData.get("spotId")) ,Long.valueOf((Integer)  spotData.get("lotId")),
                    driverId , (String) spotData.get("payment_method"));
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public void freeSpot(Map<String , Object> spotData) throws Exception {
        try {
            String jwt = (String) spotData.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long driverId = Long.parseLong(claims.getId());
            if (driverId == null)
                throw new Exception("driver id is null");
            reservationService.freeReservation(Long.valueOf((Integer) spotData.get("spotId")) ,Long.valueOf((Integer) spotData.get("lotId")), driverId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
