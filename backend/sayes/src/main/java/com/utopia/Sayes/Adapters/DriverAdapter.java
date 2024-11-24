package com.utopia.Sayes.Adapters;

import com.google.gson.Gson;
import com.utopia.Sayes.Models.Driver;

import java.util.HashMap;
import java.util.Map;

public class DriverAdapter implements IAdapter<Driver> {
    @Override
    public Map<String, Object> toMap(Driver driver) {
        Map<String, Object> driverMap = new HashMap<>();
        driverMap.put("username", driver.getUsername());
        driverMap.put("user_password",driver.getUser_password());
        driverMap.put("balance", driver.getBalance());
        driverMap.put("plate_number", driver.getPlate_number());
        driverMap.put("license_number", driver.getLicense_number());
        driverMap.put("payment_method" , driver.getPayment_method());
        return driverMap;
    }

    @Override
    public Driver fromMap(Map<String, Object> map) {
        String username = (String) map.get("username");
        String user_password = (String) map.get("user_password");
        long balance = (long) map.get("balance");
        long license_number = (long) map.get("license_number");
        String plate_number = (String) map.get("plate_number");
        Driver driver = new Driver(username , user_password , balance , plate_number , license_number);
        driver.setDriver_id((Long) map.get("Driver_id"));
        driver.setPayment_method((String) map.get("payment_method"));
        return driver;
    }

    @Override
    public String toJson(Driver driver) {
        Gson gson = new Gson();
        return gson.toJson(driver);
    }
}
