package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LotManagement {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    SpotDAO spotDAO;

    public void createLot(long manager_id , double longitude,double latitude,long revenue, long price, long num_of_available_spots, String lot_type
            , double penalty , double fee, Duration time) throws Exception{
        try {
            Lot lot = new Lot(manager_id,longitude,latitude
                    ,revenue,price,num_of_available_spots , lot_type, penalty,fee, time);
            lotDAO.addLot(lot);
        }
        catch (Exception e){
            System.out.println("Error Creating the lot");
        }
    }
    public void addSpots(long lot_id,int count){
        try {
            Lot lot = lotDAO.getLotById(lot_id);
            if (lot == null){
                throw new Exception("there is no lot with this id");
            }
            for(int i = 0;i < count;i++){
                spotDAO.addSpot(i,lot_id,"Available");
            }
        }
        catch (Exception e){
            System.out.println("Error adding the spot");
        }
    }
}
