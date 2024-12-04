package com.utopia.Sayes.Adapters;

import com.utopia.Sayes.Models.Lot;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class LotAdapter implements IAdapter<Lot>{
    @Override
    public Map<String, Object> toMap(Lot lot) {
        Map<String, Object> lotMap = new HashMap<>();
        lotMap.put("manager_id", lot.getManager_id());
        lotMap.put("location", lot.getLocation());
        lotMap.put("price", lot.getPrice());
        lotMap.put("num_of_spots", lot.getNum_of_spots());
        lotMap.put("details", lot.getDetails());
        return lotMap;
    }

    @Override
    public Lot fromMap(Map<String, Object> map) {
        long lot_id = (long) map.get("lot_id");
        long manager_id = (long) map.get("manager_id");
        String location = (String) map.get("location");
        long price = (long) map.get("price");
        long num_of_spots = (long) map.get("num_of_spots");
        String details = (String) map.get("details");
        Lot lot = new Lot(manager_id, location, price, num_of_spots);
        lot.setLot_id(lot_id);
        lot.setDetails(details);
        return lot;
    }

    @Override
    public String toJson(Lot lot) {
        Gson gson = new Gson();
        return gson.toJson(lot);
    }
}
