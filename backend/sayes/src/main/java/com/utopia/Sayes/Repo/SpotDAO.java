package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.SpotAdapter;
import com.utopia.Sayes.Models.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SpotDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SpotAdapter spotAdapter = new SpotAdapter();

    public void addSpot(long lot_id, String state){
        String query = "INSERT INTO spots " +
                " (lot_id, state) VALUES (?, ?)";
        int rows =  jdbcTemplate.update(query,lot_id,state);
        if(rows == 0){
            throw new RuntimeException("can't insert this spot");
        }
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
    @Transactional
    public Long getAndUpdateFirstAvailableSpotId(long lot_id, String currentState, String newState) {
        String selectQuery = "SELECT spot_id FROM spots WHERE lot_id = ? AND state = ? LIMIT 1 FOR UPDATE";
        Long spotId = jdbcTemplate.queryForObject(selectQuery, new Object[]{lot_id, currentState}, Long.class);
        if (spotId != null) {
            String updateQuery = "UPDATE spots SET state = ? WHERE spot_id = ?";
            jdbcTemplate.update(updateQuery, newState, spotId);
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
}
