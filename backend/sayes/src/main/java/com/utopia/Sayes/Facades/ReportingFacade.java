package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Models.Log;
import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Models.Spot;
import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.ProfileService;
import com.utopia.Sayes.Modules.ReportingService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportingFacade {
    @Autowired
    ReportingService reportingService;

    @Autowired
    ProfileService profileService;

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
   public Map<String , Object> getLotsReportForManager(String jwt) throws Exception {
       Map<String, Object> result = new HashMap<>();
       try {
           Claims claims = Authentication.parseToken(jwt);
           Long managerId = Long.parseLong(claims.getId());

           if (managerId == null) {
               throw new Exception("Manager ID is null");
           }
           List<Lot> lots = profileService.getManagerLots(managerId);

           List<Map<String, Object>> lotsMap = new ArrayList<>();
           for (Lot lot : lots) {
               List<Spot> spots = profileService.getSpotsByLotId(lot.getLot_id());
               Map<String, Object> lotData = new HashMap<>();
               lotData.put("lot_id", lot.getRevenue());
               lotData.put("revenue", lot.getRevenue());
               lotData.put("occupancy_rate" , reportingService.getOccupancyRate(lot.getLot_id()));
               lotsMap.add(lotData);
           }
           result.put("lots", lotsMap);
           return result;
       } catch (Exception e) {
           throw new Exception(e.getMessage());
       }
   }
}
