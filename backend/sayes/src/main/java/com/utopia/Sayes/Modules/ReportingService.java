package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Models.Log;
import com.utopia.Sayes.Repo.LogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingService {
    @Autowired
    LogDAO logDAO;

    public List<Log> getLogs() throws Exception {
        try {
            return logDAO.getlogs();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
