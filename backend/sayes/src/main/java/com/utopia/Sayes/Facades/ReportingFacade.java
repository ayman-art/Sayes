package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Models.Log;
import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.ReportingService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportingFacade {
    @Autowired
    ReportingService reportingService;

    public Map<String , Object> getLogs(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long adminId = Long.parseLong(claims.getId());
            if (adminId == null)
                throw new Exception("admin id is null");
            List<Log> logs = reportingService.getLogs();
            Map<String , Object> logsMap = new HashMap<>();
            logsMap.put("logs" , logs);
            return logsMap;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
