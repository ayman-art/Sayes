package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Models.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DriverDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
}
