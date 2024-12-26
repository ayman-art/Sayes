package com.utopia.Sayes.Repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PenaltyDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public void addPenalty(long lot_id, long driverId , double penalty){
        String query = "INSERT INTO penalties " +
                " (lot_id, driver_id ,penalty_amount) VALUES (?, ? , ?)";
        int rows =  jdbcTemplate.update(query,lot_id,driverId,penalty);
        if(rows == 0){
            throw new RuntimeException("can't insert this penalty");
        }
    }
    public void deletePenalty(long lot_id, long driverId){
        String query = "DELETE FROM penalties WHERE lot_id = ? AND driver_id = ?";
        int rows =  jdbcTemplate.update(query,lot_id,driverId);
        if(rows == 0){
            throw new RuntimeException("can't delete this penalty");
        }
    }
    public double getPenalty(long driverId , long lotId){
        String query = "SELECT penalty_amount FROM penalties WHERE driver_id = ? AND lot_id = ?";
        return jdbcTemplate.queryForObject(query, Double.class, driverId , lotId);
    }
    public boolean existsPenalty(long driverId, long lotId) {
        String query = "SELECT COUNT(*) FROM penalties WHERE driver_id = ? AND lot_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, driverId, lotId);
        return count != null && count > 0;
    }
}
