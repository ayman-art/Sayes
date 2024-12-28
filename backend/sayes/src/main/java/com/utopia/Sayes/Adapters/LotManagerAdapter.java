package com.utopia.Sayes.Adapters;

import com.utopia.Sayes.Models.LotManager;

import java.util.HashMap;
import java.util.Map;

public class LotManagerAdapter implements IAdapter<LotManager> {
    @Override
    public Map<String, Object> toMap(LotManager lotManager) {
        Map<String, Object> lotManagerMap = new HashMap<>();
        lotManagerMap.put("username", lotManager.getUsername());
        lotManagerMap.put("revenue", lotManager.getRevenue());
        return lotManagerMap;
    }

    @Override
    public LotManager fromMap(Map<String, Object> map) {
        String username = (String) map.get("username");
        String user_password = (String) map.get("user_password");
        long revenue = (long) map.get("revenue");
        LotManager lotManager = new LotManager(username , user_password , revenue);
        lotManager.setUser_id((Long) map.get("manager_id"));
        return lotManager;
    }


}
