package com.utopia.Sayes.Adapters;

import com.google.gson.Gson;
import com.utopia.Sayes.Models.Lot;

import java.sql.Time;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class LotAdapter implements IAdapter<Lot>{
    @Override
    public Map<String, Object> toMap(Lot lot) {
        Map<String, Object> lotMap = new HashMap<>();
        lotMap.put("manager_id", lot.getManager_id());
        lotMap.put("longitude", lot.getLongitude());
        lotMap.put("latitude", lot.getLatitude());
        lotMap.put("revenue", lot.getRevenue());
        lotMap.put("price", lot.getPrice());
        lotMap.put("num_of_spots", lot.getNum_of_spots());
        lotMap.put("details", lot.getDetails());
        lotMap.put("lot_type" , lot.getLot_type());
        lotMap.put("penalty" , lot.getPenalty());
        lotMap.put("fee" , lot.getFee());
        lotMap.put("time" , lot.getTime());
        return lotMap;
    }

    @Override
    public Lot fromMap(Map<String, Object> map) {
        long lot_id = (long) map.get("lot_id");
        long manager_id = (long) map.get("manager");
        double longitude = (double) map.get("longitude");
        double latitude = (double) map.get("latitude");
        long revenue = (long) map.get("revenue");
        double price = (double) map.get("price");
        long num_of_spots = (long) map.get("num_of_spots");
        String details = (String) map.get("details");
        String lot_type = (String) map.get("lot_type");
        double penalty = (double) map.get("penalty");
        double fee = (double) map.get("fee");
        long milliseconds = ((Time) map.get("time")).getTime();
        Duration time = Duration.ofMillis(milliseconds);
        Lot lot = new Lot(manager_id, longitude, latitude , revenue, price, num_of_spots,lot_type,penalty,fee,time);
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
