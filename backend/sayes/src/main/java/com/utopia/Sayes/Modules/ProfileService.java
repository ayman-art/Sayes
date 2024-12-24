package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Driver;
import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Models.LotManager;
import com.utopia.Sayes.Models.Spot;
import com.utopia.Sayes.Repo.DriverDAO;
import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.LotManagerDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    SpotDAO spotDAO;

    @Autowired
    LotManagerDAO lotManagerDAO;
    @Autowired
    DriverDAO driverDAO;

    public List<Lot> getManagerLots(long managerId) throws Exception {
        try {
          return lotDAO.getLotsByManager(managerId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
        public List<Spot> getSpotsByLotId(long lotId) throws Exception {
            try {
                return spotDAO.getSpotsByLotId(lotId);
            }
            catch (Exception e){
                throw new Exception(e.getMessage());
            }
    }
    public LotManager getManager(long managerId) throws Exception {
        try {
            return lotManagerDAO.getManagerById(managerId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Driver getDriver(long driverId) throws Exception {
        try {
            return driverDAO.getDriverById(driverId);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
