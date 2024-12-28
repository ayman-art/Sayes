package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Adapters.AdminAdapter;
import com.utopia.Sayes.Adapters.DriverAdapter;
import com.utopia.Sayes.Adapters.LotManagerAdapter;
import com.utopia.Sayes.Models.Lot;
import com.utopia.Sayes.Models.Spot;
import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.ProfileService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProfileFacade {
    @Autowired
    ProfileService profileService;

    LotManagerAdapter lotManagerAdapter = new LotManagerAdapter();

    DriverAdapter driverAdapter = new DriverAdapter();

    AdminAdapter adminAdapter = new AdminAdapter();

    public Map<String, Object> getManagerData(String jwt) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long managerId = Long.parseLong(claims.getId());

            if (managerId == null) {
                throw new Exception("Manager ID is null");
            }
            Map<String, Object> manager = lotManagerAdapter.toMap(profileService.getManager(managerId));
            List<Lot> lots = profileService.getManagerLots(managerId);

            List<Map<String, Object>> lotsWithSpots = new ArrayList<>();
            for (Lot lot : lots) {
                List<Spot> spots = profileService.getSpotsByLotId(lot.getLot_id());
                Map<String, Object> lotData = new HashMap<>();
                lotData.put("lot", lot);
                lotData.put("spots", spots);
                lotsWithSpots.add(lotData);
            }
            result.put("lotsWithSpots", lotsWithSpots);
            result.put("manager", manager);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return result;
    }
    public Map<String, Object> getDriverData(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long driverId = Long.parseLong(claims.getId());

            if (driverId == null) {
                throw new Exception("Driver ID is null");
            }
            return driverAdapter.toMap(profileService.getDriver(driverId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public Map<String, Object> getAdminData(String jwt) throws Exception {
        try {
            Claims claims = Authentication.parseToken(jwt);
            Long adminId = Long.parseLong(claims.getId());

            if (adminId == null) {
                throw new Exception("Admin ID is null");
            }
            return adminAdapter.toMap(profileService.getAdmin(adminId));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public void addAmount(Map<String , Object> driverData) throws Exception {
        try {
            String jwt = (String) driverData.get("jwt");
            Claims claims = Authentication.parseToken(jwt);
            Long driverId = Long.parseLong(claims.getId());

            if (driverId == null) {
                throw new Exception("Admin ID is null");
            }
            profileService.addAmount(driverId , Long.valueOf((Integer) driverData.get("amount")));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
