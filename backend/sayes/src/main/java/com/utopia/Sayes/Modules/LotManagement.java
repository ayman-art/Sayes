package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotManagement {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    SpotDAO spotDAO;

    public void createLot(long manager_id , String location,long price,long num_of_available_spots) throws Exception{
        try {
            Lot lot = new Lot(manager_id,location,price,num_of_available_spots);
            lotDAO.addLot(lot);
        }
        catch (Exception e){
            System.out.println("Error Creating the lot");
        }
    }
    public void addSpots(long lot_id,int count,String type){
        try {
            Lot lot = lotDAO.getLotById(lot_id);
            if (lot == null){
                throw new Exception("there is no lot with this id");
            }
            for(int i = 0;i < count;i++){
                spotDAO.addSpot(i,lot_id,"Available",type);
            }
        }
        catch (Exception e){
            System.out.println("Error adding the spot");
        }
    }
}
