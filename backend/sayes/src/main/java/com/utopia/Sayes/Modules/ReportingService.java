package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Log;
import com.utopia.Sayes.Repo.LogDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingService {
    @Autowired
    LogDAO logDAO;

    @Autowired
    SpotDAO spotDAO;

    public List<Log> getLogs() throws Exception {
        try {
            return logDAO.getlogs();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public double getOccupancyRate(long lotId) throws Exception {
        try {
            return spotDAO.getOccupancyRate(lotId);
        }
        catch (Exception e){
          throw new Exception(e.getMessage());
        }
    }
}
