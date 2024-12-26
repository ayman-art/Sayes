package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.ReservationAdapter;
import com.utopia.Sayes.Models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Repository
public class ReservationDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationAdapter reservationAdapter = new ReservationAdapter();
    public void addReservation(long spot_id, long lot_id, Timestamp start_time, Timestamp end_time, String state, long driver_id, double price, Connection connection) throws SQLException {
        String query = "INSERT INTO reserved_spots (spot_id, lot_id, start_time, end_time, state, driver_id, price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, spot_id);
            stmt.setLong(2, lot_id);
            stmt.setTimestamp(3, start_time);
            stmt.setTimestamp(4, end_time);
            stmt.setString(5, state);
            stmt.setLong(6, driver_id);
            stmt.setDouble(7, price);
            stmt.executeUpdate();
        }
    }
    public void deleteReservation(long spot_id, long lot_id) {
        String query = "DELETE FROM reserved_spots WHERE spot_id = ? AND lot_id = ?";
        int rows = jdbcTemplate.update(query, spot_id, lot_id);
        if (rows == 0){
            throw new RuntimeException("can't delete this reservation");
        }
    }
    public Reservation getReservation(long spot_id, long lot_id,long driver_id) throws Exception {
        String query = "SELECT * FROM reserved_spots WHERE spot_id = ? AND lot_id = ? AND driver_id = ?";
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, spot_id, lot_id,driver_id);
        if(resultMap.isEmpty()){
            throw new Exception("There is no spot with this id");
        }
        return reservationAdapter.fromMap(resultMap);
    }

    public double getReservationPrice(long spot_id, long lot_id) {
        String query = "SELECT price FROM reserved_spots WHERE spot_id = ? AND lot_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{spot_id, lot_id}, Double.class);
    }
    public void setReservationPrice(long spot_id, long lot_id, double price) {
        String query = "UPDATE reserved_spots SET price = ? WHERE spot_id = ? AND lot_id = ?";
        int rows = jdbcTemplate.update(query, price , spot_id, lot_id);
        if(rows == 0){
            throw new RuntimeException("reservation doesn't exist");
        }
    }
    public void updateSpotState(long spot_id,long lot_id, String newState) {
        String query = "UPDATE reserved_spots SET state = ? WHERE spot_id = ? AND lot_id = ?";
        int rowsUpdated = jdbcTemplate.update(query, newState, spot_id, lot_id);
        if(rowsUpdated == 0){
            throw new RuntimeException("Can't update spot state");
        }
    }

}
