package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Reservation;
import com.utopia.Sayes.Models.Spot;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.ReservationDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;

@Service
public class ReservationService {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    SpotDAO spotDAO;
    @Autowired
    ReservationDAO reservationDAO;
    public boolean reserveSpot(long lot_id , long spot_id, long driver_id, Time startTime , Time endTime) throws Exception{
        try {
            Spot spot = spotDAO.getSpotById(spot_id , lot_id);
            if (spot == null){
                throw new Exception("spot doesn't exist");
            }
            String spotStatus = spotDAO.getSpotState(spot_id , lot_id);
            if(!spotStatus.equals("Available")){
                throw new Exception("this spot is occupied or reserved");
            }
            lotDAO.decrementAvailableSpots(lot_id);
            spotDAO.updateSpotState(spot_id,lot_id,"Reserved");
            reservationDAO.addReservation(spot_id,lot_id,startTime , endTime,
                    "Reserved",driver_id);
            return true;
        }
        catch (Exception e){
            System.out.println("Couldn't reserve this spot: " + e);
        }
        return false;
    }
    public boolean useReservation(long spot_id ,long lot_id,long driver_id){
        try {
            Spot spot = spotDAO.getSpotById(spot_id , lot_id);
            if (spot == null){
                throw new Exception("spot doesn't exist");
            }
            String spotStatus = spotDAO.getSpotState(spot_id , lot_id);
            if(!spotStatus.equals("Reserved")){
                throw new Exception("this spot is not reserved");
            }
            Reservation reservation = reservationDAO.getReservation(spot_id , lot_id , driver_id);
            if (reservation == null){
                throw new Exception("There is no reservation for this spot");
            }
            spotDAO.updateSpotState(spot_id,lot_id,"Occupied");
            return true;
        }
        catch (Exception e){
            System.out.println("Couldn't reserve this spot: " + e);
        }
        return false;
    }
    public boolean freeReservation(long spot_id ,long lot_id,long driver_id){
        try {
            Spot spot = spotDAO.getSpotById(spot_id , lot_id);
            if (spot == null){
                throw new Exception("spot doesn't exist");
            }
            String spotStatus = spotDAO.getSpotState(spot_id , lot_id);
            if(spotStatus.equals("Available")){
                throw new Exception("this spot is Available");
            }
            Reservation reservation = reservationDAO.getReservation(spot_id , lot_id , driver_id);
            if (reservation == null){
                throw new Exception("There is no reservation for this spot");
            }
            spotDAO.updateSpotState(spot_id,lot_id,"Available");
            lotDAO.incrementAvailableSpots(lot_id);
            reservationDAO.deleteReservation(spot_id , lot_id);
            return true;
        }
        catch (Exception e){
            System.out.println("Couldn't free this spot: " + e);
        }
        return false;
    }
}
