package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.SpotAdapter;
import com.utopia.Sayes.Models.Spot;
import com.utopia.Sayes.enums.SpotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SpotDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SpotAdapter spotAdapter = new SpotAdapter();

    public long addSpot(long lot_id, String state) {
        String query = "INSERT INTO spots (lot_id, state) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setLong(1, lot_id);
            ps.setString(2, state);
            return ps;
        }, keyHolder);

        if (rows == 0) {
            throw new RuntimeException("Can't insert this spot");
        }

        return keyHolder.getKey().longValue();
    }

    public void updateSpotState(long spot_id,long lot_id, String newState) {
        String query = "UPDATE spots SET state = ? WHERE spot_id = ? AND lot_id = ?";
        int rowsUpdated = jdbcTemplate.update(query, newState, spot_id, lot_id);
        if(rowsUpdated == 0){
            throw new RuntimeException("Can't update spot state");
        }
    }
    public Spot getSpotById(long spot_id, long lot_id) throws Exception {
        String query = "SELECT * FROM spots WHERE spot_id = ? AND lot_id = ?";
            Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, spot_id, lot_id);
            if(resultMap.isEmpty()){
                System.out.println("here");
                throw new Exception("There is no spot with this id");
            }
            return spotAdapter.fromMap(resultMap);
    }
    public String getSpotState(long spot_id, long lot_id) {
        String query = "SELECT state FROM spots WHERE spot_id = ? AND lot_id = ?";
        String state = jdbcTemplate.queryForObject(query, new Object[]{spot_id, lot_id}, String.class);
        if(state == null){
            throw new NullPointerException("Spot Doesn't exist");
        }
        return state;
    }

    public Long getAndUpdateFirstAvailableSpotId(Connection connection, long lot_id, String currentState, String newState) throws SQLException {
        String selectQuery = "SELECT spot_id FROM spots WHERE lot_id = ? AND state = ? LIMIT 1 FOR UPDATE";
        Long spotId = null;

        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setLong(1, lot_id);
            stmt.setString(2, currentState);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    spotId = rs.getLong("spot_id");
                }
            }
        }

        if (spotId != null) {
            String updateQuery = "UPDATE spots SET state = ? WHERE spot_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                stmt.setString(1, newState);
                stmt.setLong(2, spotId);
                stmt.executeUpdate();
            }
        }

        return spotId;
    }
    public List<Spot> getSpotsByLotId(long lotId) {
        String query = "SELECT * FROM spots WHERE lot_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, lotId);
        List<Spot> spots = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Spot spot = spotAdapter.fromMap(row);
            spots.add(spot);
        }
        return spots;
    }
    public double getOccupancyRate (long lot_id){
        String query = "SELECT COUNT(*) FROM spots WHERE lot_id = ? AND state = ?";
        String query1 = "SELECT COUNT(*) FROM spots WHERE lot_id = ?";
        long occupied = jdbcTemplate.queryForObject(query ,new Object[]{lot_id, String.valueOf(SpotStatus.Occupied)},Long.class);
        long total = jdbcTemplate.queryForObject(query1 , new Object[]{lot_id} , Long.class);
        return (double) occupied / total;
    }
    public long getNumOfAvailableSpots(long lotId){
        String query = "SELECT COUNT(*) FROM spots WHERE lot_id = ? AND state = ?";
        return jdbcTemplate.queryForObject(query , new Object[]{lotId , String.valueOf(SpotStatus.Available)}
                , Long.class);
    }
}
