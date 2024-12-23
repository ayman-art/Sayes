package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.LotManagement;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class LotManagementFacade {
    @Autowired
    LotManagement lotManagement;
    public Map<String , Object> createLot(Map<String , Object> lotData) throws Exception {
        try {
            String jwt = (String) lotData.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long managerId = Long.parseLong(claims.getId());
            System.out.println(managerId);
            if (managerId == null)
                throw new Exception("manager id is null");
            long lotId = lotManagement.createLot(managerId ,(double) lotData.get("longitude")
            ,(double) lotData.get("latitude") , Long.valueOf((Integer) lotData.get("revenue")) ,
                    Long.valueOf((Integer)  lotData.get("price")),Long.valueOf((Integer)  lotData.get("num_of_spots")),
                    (String) lotData.get("lot_type") , (double)lotData.get("penalty"),
                    (double)lotData.get("fee") , Duration.parse((CharSequence) lotData.get("time")));
            Map<String , Object> data = new HashMap<>();
            data.put("lotId" , lotId);
            return data;
        }
        catch (Exception e){
           throw new Exception(e.getMessage());
        }
    }
    public void addSpot(Map<String , Object> spots) throws Exception {
        try {
            String jwt = (String) spots.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long managerId = Long.parseLong(claims.getId());
            if (managerId == null)
                throw new Exception("manager id is null");
            lotManagement.addSpots(Long.valueOf((Integer) spots.get("lotId")) ,(int) spots.get("count"));
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public Map<String , Object> getLots(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long managerId = Long.parseLong(claims.getId());
            if (managerId == null)
                throw new Exception("manager id is null");
            List<Lot> lots = lotManagement.getLots();
            Map<String , Object> data = new HashMap<>();
            data.put("lots" , lots);
            return data;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
