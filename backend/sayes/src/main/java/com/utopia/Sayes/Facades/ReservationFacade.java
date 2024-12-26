package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.ReservationService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReservationFacade {
    @Autowired
    ReservationService reservationService;

    public Map<String, Object> reserveSpot(Map<String, Object> spotData) throws Exception {
        try {
            // Extract and parse the JWT token to get the driver ID
            String jwt = (String) spotData.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long driverId = Long.parseLong(claims.getId());
            if (driverId == null) {
                throw new Exception("Driver ID is null");
            }

            // Parse the end time from the input data (HH:mm:ss format)
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            java.util.Date date = format.parse((String) spotData.get("endTime"));
            java.sql.Time sqlTime = new java.sql.Time(date.getTime());

            // Get the current date and time
            Calendar currentCalendar = Calendar.getInstance();
            int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentCalendar.get(Calendar.MINUTE);
            int currentSecond = currentCalendar.get(Calendar.SECOND);

            // Extract the hour, minute, second from the provided endTime
            int endHour = sqlTime.getHours();
            int endMinute = sqlTime.getMinutes();
            int endSecond = sqlTime.getSeconds();

            // Compare the provided end time with the current time
            if (endHour > currentHour || (endHour == currentHour && endMinute > currentMinute) ||
                    (endHour == currentHour && endMinute == currentMinute && endSecond > currentSecond)) {
                // If end time is later than current time, it is today
                currentCalendar.set(Calendar.HOUR_OF_DAY, endHour);
                currentCalendar.set(Calendar.MINUTE, endMinute);
                currentCalendar.set(Calendar.SECOND, endSecond);
            } else {
                // If end time is earlier or equal, it is tomorrow
                currentCalendar.add(Calendar.DAY_OF_YEAR, 1);  // Add 1 day to make it tomorrow
                currentCalendar.set(Calendar.HOUR_OF_DAY, endHour);
                currentCalendar.set(Calendar.MINUTE, endMinute);
                currentCalendar.set(Calendar.SECOND, endSecond);
            }

            // Create the final timestamp (year-month-day time)
            Timestamp endTimestamp = new Timestamp(currentCalendar.getTimeInMillis());

            // Call the service to reserve the spot
            long spotId = reservationService.reserveSpot(Long.valueOf((Integer) spotData.get("lotId")), driverId, endTimestamp);

            // Prepare the response data
            Map<String, Object> data = new HashMap<>();
            data.put("spotId", spotId);
            return data;

        } catch (Exception e) {
            throw new Exception("Error during reservation: " + e.getMessage());
        }
    }
    public void useSpot(Map<String , Object> spotData) throws Exception {
        try {
            String jwt = (String) spotData.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long driverId = Long.parseLong(claims.getId());
            if (driverId == null)
                throw new Exception("driver id is null");
            reservationService.useReservation(Long.valueOf((Integer)  spotData.get("spotId")) ,Long.valueOf((Integer)  spotData.get("lotId")), driverId);
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
