package com.utopia.Sayes.Repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FeeDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public void addFee(long lot_id, long driverId , double fee){
        String query = "INSERT INTO fees " +
                " (lot_id, driver_id ,fee) VALUES (?, ? , ?)";
        int rows =  jdbcTemplate.update(query,lot_id,driverId,fee);
        if(rows == 0){
            throw new RuntimeException("can't insert this fee");
        }
    }
    public void deleteFee(long lot_id, long driverId){
        String query = "DELETE FROM fees WHERE lot_id = ? AND driver_id = ?";
        int rows =  jdbcTemplate.update(query,lot_id,driverId);
        if(rows == 0){
            throw new RuntimeException("can't delete this fee");
        }
    }
    public double getFee(long driverId , long lotId){
        String query = "SELECT fee FROM fees WHERE driver_id = ? AND lot_id = ?";
        return jdbcTemplate.queryForObject(query, Double.class, driverId , lotId);
    }
    public boolean existsFee(long driverId, long lotId) {
        String query = "SELECT COUNT(*) FROM fees WHERE driver_id = ? AND lot_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, driverId, lotId);
        return count != null && count > 0;
    }
    public void updateFee(long driverId , long lotId , double fee) {
        String query = "UPDATE fees SET fee = fee + ? WHERE driver_id = ? AND lot_id ";
        int rows  = jdbcTemplate.update(query,fee,driverId,lotId);
        if(rows == 0){
            throw new RuntimeException("error updating fee");
        }
    }
}
