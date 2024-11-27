package com.utopia.Sayes.Repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpotDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addSpot(long spot_id , long lot_id, String state, String type){
        String query = "INSERT INTO spots " +
                " (spot_id, lot_id, state, spot_type) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query,spot_id,lot_id,state,type);
        }
        catch (Exception e){
            System.out.println("Error while adding spot: " + e);
        }
    }
    public void updateSpotState(long spot_id,long lot_id, String newState) {
        String query = "UPDATE spots SET state = ? WHERE spot_id = ? AND lot_id = ?";
        try {
            int rowsUpdated = jdbcTemplate.update(query, newState, spot_id, lot_id);
        } catch (Exception e) {
            System.out.println("Error while updating spot state: " + e);
        }
    }
    public void getSpotById(long spot_id,long lot_id){
        String query = "SELECT * FROM spots WHERE spot_id = ? AND lot_id = ?";
        try {
            jdbcTemplate.update(query,spot_id, lot_id);
        } catch (Exception e) {
            System.out.println("Error while Selecting spot : " + e);
        }
    }
    public String getSpotState(long spot_id, long lot_id) {
        String query = "SELECT state FROM spots WHERE spot_id = ? AND lot_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{spot_id, lot_id}, String.class);
        } catch (Exception e) {
            System.out.println("Error while fetching spot state: " + e);
            return null;
        }
    }

}
