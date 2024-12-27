package com.utopia.Sayes.Modules;

import com.utopia.Sayes.DTOs.UpdateDriverReservationDTO;
import com.utopia.Sayes.DTOs.UpdateLotDTO;
import com.utopia.Sayes.DTOs.UpdateLotManagerLotSpotsDTO;
import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Models.Reservation;
import com.utopia.Sayes.Models.Spot;
import com.utopia.Sayes.Modules.DynamicPricing.DynamicPricing;
import com.utopia.Sayes.Modules.WebSocket.NotificationService;
import com.utopia.Sayes.Repo.*;
import com.utopia.Sayes.enums.SpotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ReservationService {
    @Autowired
    LotDAO lotDAO;

    @Autowired
    SpotDAO spotDAO;

    @Autowired
    ReservationDAO reservationDAO;

    @Autowired
    DynamicPricing dynamicPricing;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ViolationService violationService;

    @Autowired
    LogDAO logDAO;

    @Autowired
    FeeDAO feeDAO;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public long reserveSpot(long lot_id, long driver_id, Timestamp endTime) throws Exception {
        Connection connection = null;
        CallableStatement callableStatement = null;

        java.sql.Timestamp startTimestamp = new java.sql.Timestamp(new Date().getTime());
        java.sql.Timestamp endTimestamp = new java.sql.Timestamp(endTime.getTime());
        double price = dynamicPricing.getPrice(
                lot_id,
                new Time(startTimestamp.getTime()),
                new Time(endTimestamp.getTime())
                ,driver_id);
        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();

            callableStatement = connection.prepareCall("{CALL ReserveSpot(?, ?, ?, ?, ?)}");

            callableStatement.setLong(1, lot_id);
            callableStatement.setLong(2, driver_id);
            callableStatement.setTimestamp(3, endTime);
            callableStatement.setDouble(4, price);

            callableStatement.registerOutParameter(5, Types.BIGINT);

            callableStatement.execute();

            long spotId = callableStatement.getLong(5);

            if (spotId == 0) {
                throw new Exception("Failed to reserve a spot: No available spot found.");
            }

            Lot lot = lotDAO.getLotById(lot_id);
            notificationService.notifyLotUpdate(new UpdateLotDTO(
                    lot_id,
                    lot.getNum_of_spots(),
                    lot.getLongitude(),
                    lot.getLatitude(),
                    lot.getPrice(),
                    lot.getLot_type()
            ));
            notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                    spotId,
                    lot_id,
                    lotDAO.getLotManagerIdByLotId(lot_id),
                    lotDAO.getLotRevenue(lot_id),
                    SpotStatus.Reserved,
                    null
            ));
            setNearExpired(lot_id , spotId , driver_id);
            setReservationTimeOut(lot_id , spotId , driver_id);
            return spotId;

        } catch (SQLException sqlEx) {
            throw new Exception("Error while reserving spot: " + sqlEx.getMessage(), sqlEx);
        } finally {
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error closing CallableStatement: " + closeEx.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error closing Connection: " + closeEx.getMessage());
                }
            }
        }
    }

    public void useReservation(long spot_id ,long lot_id,long driver_id, String method) throws Exception {
        try {
            double price = reservationDAO.getReservationPrice(spot_id, lot_id);
            if (paymentService.confirmReservation(price, driver_id, lot_id , method)) {
                Spot spot = spotDAO.getSpotById(spot_id, lot_id);
                System.out.println(spot.getSpot_id());
                if (spot == null) {
                    throw new Exception("spot doesn't exist");
                }
                String spotStatus = spotDAO.getSpotState(spot_id, lot_id);
                if (!spotStatus.equals(String.valueOf(SpotStatus.Reserved))) {
                    throw new Exception("this spot is not reserved");
                }
                Reservation reservation = reservationDAO.getReservation(spot_id, lot_id, driver_id);
                if (reservation == null) {
                    throw new Exception("There is no reservation for this spot");
                }
                reservationDAO.updateSpotState(spot_id , lot_id , String.valueOf(SpotStatus.Occupied));
                spotDAO.updateSpotState(spot_id, lot_id, String.valueOf(SpotStatus.Occupied));
                setOverOccupiedTime(lot_id, spot_id, driver_id);

                notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                        spot_id,
                        lot_id,
                        lotDAO.getLotManagerIdByLotId(lot_id),
                        lotDAO.getLotRevenue(lot_id),
                        SpotStatus.Occupied,
                        null));
            }
            else{
                freeReservation(spot_id , lot_id , driver_id);
                throw new Exception("driver doesn't have enough balance");
            }
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public void freeReservation(long spot_id ,long lot_id,long driver_id) throws Exception {
        try {
            Spot spot = spotDAO.getSpotById(spot_id , lot_id);
            if (spot == null){
                throw new Exception("spot doesn't exist");
            }
            String spotStatus = spotDAO.getSpotState(spot_id , lot_id);
            if(spotStatus.equals(String.valueOf(SpotStatus.Available))){
                throw new Exception("this spot is Available");
            }
            Reservation reservation = reservationDAO.getReservation(spot_id , lot_id , driver_id);
            if (reservation == null){
                throw new Exception("There is no reservation for this spot");
            }
            violationService.takeFeeAmount(driver_id , lot_id);
            spotDAO.updateSpotState(spot_id,lot_id, String.valueOf(SpotStatus.Available));
            //reservationDAO.deleteReservation(spot_id , lot_id);
            Date date = Date.from(reservation.getStart_time().atZone(ZoneId.systemDefault()).toInstant());
            java.sql.Timestamp endTimestamp = new java.sql.Timestamp(new Date().getTime());
            logDAO.addlog(spot_id , lot_id, date , endTimestamp,driver_id);
            Lot lot = lotDAO.getLotById(lot_id);
            notificationService.notifyLotUpdate(new UpdateLotDTO(lot_id, lot.getNum_of_spots(),
                    lot.getLongitude(), lot.getLatitude(), lot.getPrice(), lot.getLot_type()));
            notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                    spot_id,
                    lot_id,
                    lotDAO.getLotManagerIdByLotId(lot_id),
                    lotDAO.getLotRevenue(lot_id),
                    SpotStatus.Available,
                    null));
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    private void setReservationTimeOut(long lot_id, long spot_id, long driver_id) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Time timeLimit = lotDAO.getTimeLimitById(lot_id);
        long timeoutMinutes = timeLimit.toLocalTime().toSecondOfDay() / 60;

        scheduler.schedule(() -> {
            try {
                if (reservationDAO.existsReservation(driver_id , lot_id , spot_id)){
                    Reservation reservation = reservationDAO.getReservation(spot_id, lot_id, driver_id);
                    String reservationState = reservation.getState();
                    if (String.valueOf(SpotStatus.Reserved).equals(reservationState)) {
                        System.out.println(reservationState);
                        freeReservation(spot_id, lot_id, driver_id);
                        System.out.println("Reservation expired and spot is now available again.");
                        double penalty = lotDAO.getLotPenalty(lot_id);
                        if (!violationService.takePenaltyAmount(driver_id, lot_id, penalty))
                            violationService.addPenalty(driver_id, lot_id, penalty);
                        // send a penalty to the driver using his socket
                        notificationService.notifyDriverReservation(new UpdateDriverReservationDTO(
                                driver_id,
                                lot_id,
                                spot_id,
                                SpotStatus.ReservationTimeOut,
                                penalty,
                                -1,
                                0L,
                                null)
                        );
                        notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                            spot_id,
                            lot_id,
                            lotDAO.getLotManagerIdByLotId(lot_id),
                            lotDAO.getLotRevenue(lot_id),
                            SpotStatus.Available,
                                null));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error while checking reservation: " + e.getMessage());
            }
        }, timeoutMinutes, TimeUnit.MINUTES);
    }


    private void setOverOccupiedTime(long lot_id, long spot_id, long driver_id) throws Exception {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Reservation reservation = reservationDAO.getReservation(spot_id, lot_id, driver_id);
        LocalDateTime endTime = reservation.getEnd_time();
        Instant endTimeInstant = endTime.atZone(ZoneId.systemDefault()).toInstant(); // Convert to Instant
        long endTimeMillis = endTimeInstant.toEpochMilli(); // Get milliseconds since epoch

        Date currentTime = new Date();
        long currentTimeMillis = currentTime.getTime();
        long difference = endTimeMillis - currentTimeMillis; // In milliseconds
        System.out.println(difference);
        long initialDelay = difference / 1000;
        System.out.println(initialDelay);
        long interval = 60;

        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (reservationDAO.existsReservation(driver_id , lot_id , spot_id)){
                String spotState = spotDAO.getSpotState(spot_id , lot_id);
                if (String.valueOf(SpotStatus.Occupied).equals(spotState) ||
                        String.valueOf(SpotStatus.OverOccupied).equals(spotState)) {

                    if (String.valueOf(SpotStatus.Occupied).equals(spotState)) {
                        spotDAO.updateSpotState(spot_id, lot_id, String.valueOf(SpotStatus.OverOccupied));
                        reservationDAO.updateSpotState(spot_id, lot_id, String.valueOf(SpotStatus.OverOccupied));
                    }
                    violationService.updateFee(driver_id, lot_id);
                    System.out.println("here");
                    System.out.println("Reservation is over-occupied.");

                    notificationService.notifyDriverReservation(new UpdateDriverReservationDTO(
                            driver_id,
                            lot_id,
                            spot_id,
                            SpotStatus.OverOccupied,
                            feeDAO.getFee(driver_id, lot_id),
                            reservationDAO.getReservationPrice(spot_id, lot_id),
                            0L,
                            0L)
                    );

                    notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                            spot_id,
                            lot_id,
                            lotDAO.getLotManagerIdByLotId(lot_id),
                            lotDAO.getLotRevenue(lot_id),
                            SpotStatus.OverOccupied,
                            null));
                }
                }else{
                    scheduler.shutdown();
                    System.out.println("Scheduler shut down as reservation no longer exists.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }, initialDelay, interval, TimeUnit.SECONDS);
    }
    private void setNearExpired(long lot_id, long spot_id, long driver_id) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Time timeLimit = lotDAO.getTimeLimitById(lot_id);
        long totalTimeoutMinutes = ((timeLimit.toLocalTime().toSecondOfDay()));
        long timeoutMinutes = (long) (totalTimeoutMinutes * 0.75);
        long remaining = totalTimeoutMinutes - timeoutMinutes;

        scheduler.schedule(() -> {
            try {
                if (reservationDAO.existsReservation(driver_id , lot_id , spot_id)){
                    Reservation reservation = reservationDAO.getReservation(spot_id, lot_id, driver_id);
                    String reservationState = reservation.getState();
                    if (String.valueOf(SpotStatus.Reserved).equals(reservationState)) {
                        System.out.println("nearExpiry");
                        notificationService.notifyDriverReservation(new UpdateDriverReservationDTO(
                                driver_id,
                                lot_id,
                                spot_id,
                                SpotStatus.NearExpiry,
                                0,
                                -1,
                                remaining,
                                null)
                        );
                    }
                }
            } catch (Exception e) {
                System.err.println("Error while checking reservation: " + e.getMessage());
            }
        }, timeoutMinutes, TimeUnit.SECONDS);
    }

}
