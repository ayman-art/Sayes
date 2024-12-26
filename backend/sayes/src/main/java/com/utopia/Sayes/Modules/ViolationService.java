package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Repo.DriverDAO;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.PenaltyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViolationService {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    PenaltyDAO penaltyDAO;
    @Autowired
    DriverDAO driverDAO;

    public boolean takePenaltyAmount(long driverId, long lotId , double penalty){
        double balance = driverDAO.getDriverBalance(driverId);
        if(balance >= penalty){
            driverDAO.updateDriverBalance((long) penalty , driverId);
            lotDAO.updateLotRevenue((long) penalty , lotId);
            return true;
        }
        return false;
    }
    public void addPenalty(long driverId, long lotId , double penalty) throws Exception {
        try {
            penaltyDAO.addPenalty(lotId , driverId, penalty);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public double getPenalty(long driverId , long lotId){
        return penaltyDAO.getPenalty(driverId , lotId);
    }
}
