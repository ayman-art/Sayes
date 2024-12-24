package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.DriverAdapter;
import com.utopia.Sayes.Models.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DriverDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    DriverAdapter driverAdapter = new DriverAdapter();

    public void addDriver(Driver driver) {
        String query = "INSERT INTO Drivers " +
                "(Driver_id, plate_number, balance, payment_method, license_number) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, driver.getUser_id(), driver.getPlate_number(), driver.getBalance(),
                driver.getPayment_method(), driver.getLicense_number());
    }

    public boolean doesPlateNumberExist(String plateNumber) {
        String query = "SELECT EXISTS(SELECT 1 FROM Drivers WHERE plate_number = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, plateNumber));
    }

    public boolean doesLicenseNumberExist(long licenseNumber) {
        String query = "SELECT EXISTS(SELECT 1 FROM Drivers WHERE license_number = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, licenseNumber));
    }
    public Long getDriverBalance(long driverId) {
        String query = "SELECT balance FROM Drivers WHERE Driver_id = ? ";
        Long balance = jdbcTemplate.queryForObject(query, new Object[]{driverId}, Long.class);
        if(balance == null){
            throw new NullPointerException("balance Doesn't exist");
        }
        return balance;
    }
    public void updateDriverBalance(long price , long driverId) {
        String query = "UPDATE Drivers SET balance = balance - ? WHERE Driver_id = ? ";
        int rows  = jdbcTemplate.update(query, price, driverId);
        if(rows == 0){
            throw new RuntimeException("error updating balance");
        }
    }
    public Driver getDriverById(long driverId) throws Exception {
        String query = """
                SELECT * FROM Users
                LEFT JOIN Drivers ON Users.user_id = Drivers.Driver_id
                WHERE user_id = ?
                """;
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, driverId);
        if(resultMap.isEmpty()){
            throw new Exception("error");
        }
        return driverAdapter.fromMap(resultMap);
    }
}
