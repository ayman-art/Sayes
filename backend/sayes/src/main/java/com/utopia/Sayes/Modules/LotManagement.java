package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.LotManagerDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class LotManagement {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    SpotDAO spotDAO;

    @Autowired
    LotManagerDAO lotManagerDAO;

    public long createLot(long manager_id , double longitude,double latitude,long revenue, long price, String lot_type
            , double penalty , double fee, Duration time , int numOfSpots) throws Exception{
        try {
            if (! lotManagerDAO.doesManagerExist(manager_id))
                throw new Exception("lot manager is not exist");
            Lot lot = new Lot(manager_id,longitude,latitude
                    ,revenue,price,lot_type, penalty,fee, time);
            long lotId =  lotDAO.addLot(lot);
            addSpots(lotId , numOfSpots);
            return lotId;
        }
        catch (Exception e){
           throw new Exception(e.getMessage());
        }
    }
    public void addSpots(long lot_id, int count) throws Exception {
        try {
            Lot lot = lotDAO.getLotById(lot_id);
            System.out.println(lot.getLot_id());
            if (lot == null){
                throw new Exception("there is no lot with this id");
            }
            for(int i = 0;i < count;i++){
                spotDAO.addSpot(lot_id,"Available");
            }
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Lot> getLots() throws Exception {
        try {
            return lotDAO.getLots();
        }
        catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }
}
