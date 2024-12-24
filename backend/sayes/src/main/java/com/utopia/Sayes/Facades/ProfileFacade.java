package com.utopia.Sayes.Facades;

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

}
