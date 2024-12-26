package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Repo.DriverDAO;
import com.utopia.Sayes.Repo.FeeDAO;
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
    @Autowired
    FeeDAO feeDAO;

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
    public void takeFeeAmount(long driverId, long lotId){
        if(feeDAO.existsFee(driverId , lotId)){
            double fee = feeDAO.getFee(driverId , lotId);
            feeDAO.deleteFee(lotId , driverId);
            lotDAO.updateLotRevenue((long) fee , lotId);
        }
    }
    public void updateFee(long driverId , long lotId){
        double fee = lotDAO.getLotFee(lotId);
        if(feeDAO.existsFee(driverId , lotId)){
            feeDAO.updateFee(driverId , lotId , fee);
        }
        else feeDAO.addFee(lotId ,driverId ,fee);
    }
}
