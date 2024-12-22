package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Repo.DriverDAO;
import com.utopia.Sayes.Repo.LotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    DriverDAO driverDAO;

    @Autowired
    LotDAO lotDAO;

    private boolean checkBalance(double price, long driverId){
        long balane = driverDAO.getDriverBalance(driverId);
        return balane >= (long) price;
    }
    public boolean confirmReservation(double price, long driverId, long lotId){
        if (checkBalance(price , driverId)){
            driverDAO.updateDriverBalance((long) price, driverId);
            lotDAO.updateLotRevenue((long) price, lotId);
            return true;
        }
        return false;
    }

}
