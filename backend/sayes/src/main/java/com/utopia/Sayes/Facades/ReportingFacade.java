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

import java.io.ByteArrayOutputStream;
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
               lotData.put("lot_id", lot.getLot_id());
               lotData.put("revenue", lot.getRevenue());
               lotData.put("longitude" , lot.getLongitude());
               lotData.put("latitude" , lot.getLatitude());
               lotData.put("lot_type" , lot.getLot_type());
               lotData.put("price" , lot.getPrice());
               lotData.put("num_of_spots" , lot.getNum_of_spots());
               lotData.put("occupancy_rate" , reportingService.getOccupancyRate(lot.getLot_id()));
               lotsMap.add(lotData);
           }
           result.put("lots", lotsMap);
           return result;
       } catch (Exception e) {
           throw new Exception(e.getMessage());
       }
   }
   public Map<String , Object> getTopUsers(String jwt) throws Exception {
       try {
           Claims claims = Authentication.parseToken(jwt);
           Long adminId = Long.parseLong(claims.getId());

           if (adminId == null) {
               throw new Exception("Admin ID is null");
           }
          List<Map<String , Object>> topUsers = reportingService.getTopUsers();
          Map<String , Object> topUsersMap = new HashMap<>();
          topUsersMap.put("topUsers" , topUsers);
          return topUsersMap;
       } catch (Exception e) {
           throw new Exception(e.getMessage());
       }
   }
    public Map<String , Object> getTopLots(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long adminId = Long.parseLong(claims.getId());

            if (adminId == null) {
                throw new Exception("Admin ID is null");
            }
            List<Map<String , Object>> topUsers = reportingService.getTopLots();
            Map<String , Object> topLotsMap = new HashMap<>();
            topLotsMap.put("topLots" , topUsers);
            return topLotsMap;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public byte[] getTopUsersReport(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long adminId = Long.parseLong(claims.getId());

            if (adminId == null) {
                throw new Exception("Admin ID is null");
            }
            ByteArrayOutputStream reportStream = reportingService.generateTopUsersReport();
            return reportStream.toByteArray();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public byte[] getTopLotsReport(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long adminId = Long.parseLong(claims.getId());

            if (adminId == null) {
                throw new Exception("Admin ID is null");
            }
            ByteArrayOutputStream reportStream = reportingService.generateTopLotsReport();
            return reportStream.toByteArray();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public byte[] getViolationsReport(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long adminId = Long.parseLong(claims.getId());

            if (adminId == null) {
                throw new Exception("Admin ID is null");
            }
            ByteArrayOutputStream reportStream = reportingService.generateViolationsReport();
            return reportStream.toByteArray();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public byte[] generateLotsReportForManager(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long managerId = Long.parseLong(claims.getId());

            if (managerId == null) {
                throw new Exception("Manager ID is null");
            }
            List<Lot> lots = profileService.getManagerLots(managerId);

            List<Map<String, Object>> lotsMap = new ArrayList<>();
            for (Lot lot : lots) {
                Map<String, Object> lotData = new HashMap<>();
                lotData.put("lot_id", lot.getLot_id());
                lotData.put("revenue", lot.getRevenue());
                lotData.put("lot_type" , lot.getLot_type());
                lotData.put("price" , lot.getPrice());
                lotData.put("num_of_spots" , lot.getNum_of_spots());
                lotData.put("occupancy_rate" , reportingService.getOccupancyRate(lot.getLot_id()));
                lotsMap.add(lotData);
            }
            ByteArrayOutputStream output = reportingService.generateLotsReport(lotsMap);
            return output.toByteArray();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
