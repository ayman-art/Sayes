package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.LotAdapter;
import com.utopia.Sayes.Models.Lot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LotDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LotAdapter lotAdapter = new LotAdapter();

    public long addLot(Lot lot) {
        String query = "INSERT INTO Lots " +
                "(manager, longitude, latitude, revenue, price, num_of_spots, details, lot_type, penalty, fee, time)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, lot.getManager_id());
            ps.setDouble(2, lot.getLongitude());
            ps.setDouble(3, lot.getLatitude());
            ps.setLong(4, lot.getRevenue());
            ps.setDouble(5, lot.getPrice());
            ps.setLong(6, lot.getNum_of_spots());
            ps.setString(7, lot.getDetails());
            ps.setString(8, lot.getLot_type());
            ps.setDouble(9, lot.getPenalty());
            ps.setDouble(10, lot.getFee());
            ps.setObject(11, lot.getTime());
            return ps;
        }, keyHolder);

        if (rows == 0) {
            throw new RuntimeException("Can't add this lot");
        }

        return keyHolder.getKey().longValue(); // Retrieve the generated key as a long.
    }

    public List<Lot> getLotsByManager(int managerId) {
        String query = "SELECT * FROM Lots WHERE manager = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, managerId);
        if(rows.isEmpty()){
            throw new IllegalStateException("this manager has no lots");
        }
        List<Lot> lots = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Lot lot = lotAdapter.fromMap(row);
            lots.add(lot);
        }
        return lots;
    }
    public void decrementAvailableSpots(long lotId) {
        String query = "UPDATE Lots SET num_of_spots = num_of_spots - 1 WHERE lot_id = ? AND num_of_spots > 0";
        int rowsUpdated = jdbcTemplate.update(query, lotId);
        if (rowsUpdated == 0) {
            throw new IllegalStateException("No available spots: " + lotId);
        }
    }
    public void incrementAvailableSpots(long lotId) {
        String query = "UPDATE Lots SET num_of_spots = num_of_spots + 1 WHERE lot_id = ?";
        int rowsUpdated = jdbcTemplate.update(query, lotId);
        if (rowsUpdated == 0) {
            throw new IllegalStateException("Can't increment spots fro this manager: " + lotId);
        }
    }

    public Lot getLotById(long lot_id) throws Exception {
        String query = "SELECT * FROM Lots WHERE lot_id = ?";
            Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,lot_id);
            if (resultMap.isEmpty()){
                throw new Exception("there is no lot with this id");
            }

            return lotAdapter.fromMap(resultMap);
    }

    public long getLotTotalSpots(long lot_id){
        String query = "SELECT COUNT(*) FROM spots WHERE lot_id = ?";
        return jdbcTemplate.queryForObject(query, Long.class, lot_id);
    }
    public Time getTimeLimitById(long lotId) {
        String query = "SELECT time FROM Lots WHERE lot_id = ?";
            return jdbcTemplate.queryForObject(query, Time.class, lotId);
    }

    public double getLotPenalty(long lot_id){
        String query = "SELECT penalty FROM Lots WHERE lot_id = ?";
        return jdbcTemplate.queryForObject(query, Double.class, lot_id);
    }
}
