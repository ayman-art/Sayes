package com.utopia.Sayes.Repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void addReservation(long spot_id, long lot_id, String reservation_time, String state, long driver_id) {
        String query = "INSERT INTO reserved_spots"  +
        "(spot_id, lot_id, reservation_time, state, driver_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query, spot_id, lot_id, reservation_time, state, driver_id);
        } catch (Exception e) {
            System.out.println("Error while adding reservation: " + e);
        }
    }
    public void deleteReservation(long spot_id, long lot_id) {
        String query = "DELETE FROM reserved_spots WHERE spot_id = ? AND lot_id = ?";
        try {
            jdbcTemplate.update(query, spot_id, lot_id);
        } catch (Exception e) {
            System.out.println("Error while deleting reservation: " + e);
        }
    }

}
