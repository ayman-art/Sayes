package com.utopia.Sayes.Modules;

import com.utopia.Sayes.DTOs.UpdateDriverReservationDTO;
import com.utopia.Sayes.DTOs.UpdateLotDTO;
import com.utopia.Sayes.DTOs.UpdateLotManagerLotSpotsDTO;
import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Models.Reservation;
import com.utopia.Sayes.Models.Spot;
import com.utopia.Sayes.Modules.DynamicPricing.DynamicPricing;
import com.utopia.Sayes.Modules.WebSocket.NotificationService;
import com.utopia.Sayes.Repo.LogDAO;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.ReservationDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import com.utopia.Sayes.enums.SpotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
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
    private NotificationService notificationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public long reserveSpot(long lot_id, long driver_id, Timestamp endTime) throws Exception {
        Connection connection = null;
        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            connection.setAutoCommit(false);

            long spotId = spotDAO.getAndUpdateFirstAvailableSpotId(connection, lot_id,
                    String.valueOf(SpotStatus.Available), String.valueOf(SpotStatus.Reserved));

            if (spotId == 0) {
                throw new Exception("Spot doesn't exist or is unavailable.");
            }

            java.sql.Timestamp startTimestamp = new java.sql.Timestamp(new Date().getTime());
            java.sql.Timestamp endTimestamp = new java.sql.Timestamp(endTime.getTime());
            double price = dynamicPricing.getPrice(lot_id,
                    new Time(startTimestamp.getTime()),
                    new Time(endTimestamp.getTime())
                    ,driver_id);
            reservationDAO.addReservation(spotId, lot_id, startTimestamp, endTimestamp,
                    String.valueOf(SpotStatus.Reserved), driver_id, price, connection);
            //spotDAO.updateSpotState(spotId,lot_id, String.valueOf(SpotStatus.Reserved));
            System.out.println(lot_id);
            setReservationTimeOut(lot_id , spotId , driver_id);
           connection.commit();
            Lot lot = lotDAO.getLotById(lot_id);
            notificationService.notifyLotUpdate(new UpdateLotDTO(lot_id, lot.getNum_of_spots(),
                    lot.getLongitude(), lot.getLatitude(), lot.getPrice(), lot.getLot_type()));
            notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                    spotId, lot_id, lotDAO.getLotManagerIdByLotId(lot_id),
                    lotDAO.getLotRevenue(lot_id), SpotStatus.Reserved));

            return spotId;

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new Exception("Error rolling back the transaction: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw new Exception("Error while reserving spot: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error closing the connection: " + closeEx.getMessage());
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
                spotDAO.updateSpotState(spot_id, lot_id, String.valueOf(SpotStatus.Occupied));
                setOverOccupiedTime(lot_id, spot_id, driver_id);

                notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                        spot_id,
                        lot_id,
                        lotDAO.getLotManagerIdByLotId(lot_id),
                        lotDAO.getLotRevenue(lot_id),
                        SpotStatus.Occupied));
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
            reservationDAO.deleteReservation(spot_id , lot_id);
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
                    SpotStatus.Available));
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
                Reservation reservation = reservationDAO.getReservation(spot_id, lot_id, driver_id);
                String reservationState = reservation.getState();
                if (String.valueOf(SpotStatus.Reserved).equals(reservationState)) {
                    freeReservation(spot_id , lot_id , driver_id);
                    System.out.println("Reservation expired and spot is now available again.");
                    double penalty = lotDAO.getLotPenalty(lot_id);
                    if (!violationService.takePenaltyAmount(driver_id , lot_id , penalty))
                        violationService.addPenalty(driver_id , lot_id , penalty);
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
                            SpotStatus.Available));
                }
            } catch (Exception e) {
                System.err.println("Error while checking reservation: " + e.getMessage());
            }
        }, timeoutMinutes, TimeUnit.MINUTES);
    }


    private void setOverOccupiedTime(long lot_id, long spot_id, long driver_id) throws Exception {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Reservation reservation = reservationDAO.getReservation(spot_id, lot_id, driver_id);
        String reservationState = reservation.getState();
        LocalDateTime endTime = reservation.getEnd_time();
        Date currentTime = new Date();
        long difference = endTime.getSecond() * 1000 - currentTime.getTime();
        long minutesElapsed = difference / (60 * 1000);
        scheduler.schedule(() -> {
            try {
                if (String.valueOf(SpotStatus.Occupied).equals(reservationState) ||
                String.valueOf(SpotStatus.OverOccupied).equals(reservationState)) {
                    if (String.valueOf(SpotStatus.Occupied).equals(reservationState)){
                        spotDAO.updateSpotState(spot_id , lot_id , String.valueOf(SpotStatus.OverOccupied));
                        reservationDAO.updateSpotState(spot_id , lot_id,String.valueOf(SpotStatus.OverOccupied));
                    }
                    violationService.updateFee(driver_id , lot_id);
                    System.out.println("Reservation is over-occupied.");
                    // send a fee to the driver using his socket
                    notificationService.notifyDriverReservation(new UpdateDriverReservationDTO(
                            driver_id,
                            lot_id,
                            spot_id,
                            SpotStatus.OverOccupied,
                            lotDAO.getLotPenalty(lot_id),
                            reservationDAO.getReservationPrice(spot_id, lot_id),
                            0L,
                            0L)
                    );

                    notificationService.notifyLotManager(new UpdateLotManagerLotSpotsDTO(
                            spot_id,
                            lot_id,
                            lotDAO.getLotManagerIdByLotId(lot_id),
                            lotDAO.getLotRevenue(lot_id),
                            SpotStatus.Occupied));
                }
            } catch (Exception e) {
                System.err.println("Error while checking reservation: " + e.getMessage());
            }
        }, minutesElapsed, TimeUnit.MINUTES);
    }

}
