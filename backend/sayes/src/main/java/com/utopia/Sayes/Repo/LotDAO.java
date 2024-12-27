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

    @Autowired
    SpotDAO spotDAO;

    public long addLot(Lot lot) {
        String query = "INSERT INTO Lots " +
                "(manager, longitude, latitude, revenue, price, details, lot_type, penalty, fee, time)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        long hours = lot.getTime().toHours();
        long minutes = lot.getTime().toMinutesPart(); // Java 9+ feature
        long seconds = lot.getTime().toSecondsPart(); // Java 9+ feature

        String formatted =  String.format("%02d:%02d:%02d", hours, minutes, seconds);
        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, lot.getManager_id());
            ps.setDouble(2, lot.getLongitude());
            ps.setDouble(3, lot.getLatitude());
            ps.setLong(4, lot.getRevenue());
            ps.setDouble(5, lot.getPrice());
            ps.setString(6, lot.getDetails());
            ps.setString(7, lot.getLot_type());
            ps.setDouble(8, lot.getPenalty());
            ps.setDouble(9, lot.getFee());
            ps.setString(10, formatted);
            return ps;
        }, keyHolder);

        if (rows == 0) {
            throw new RuntimeException("Can't add this lot");
        }

        return keyHolder.getKey().longValue(); // Retrieve the generated key as a long.
    }

    public List<Lot> getLotsByManager(long managerId) {
        String query = "SELECT * FROM Lots WHERE manager = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, managerId);
        List<Lot> lots = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            row.put("num_of_spots" , spotDAO.getNumOfAvailableSpots((Long) row.get("lot_id")));
            Lot lot = lotAdapter.fromMap(row);
            lots.add(lot);
        }
        return lots;
    }
    public List<Lot> getLots() {
        String query = "SELECT * FROM Lots";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        if(rows.isEmpty()){
            throw new IllegalStateException("there is no lots");
        }
        List<Lot> lots = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            row.put("num_of_spots" , spotDAO.getNumOfAvailableSpots((Long) row.get("lot_id")));
            Lot lot = lotAdapter.fromMap(row);
            lots.add(lot);
        }
        return lots;
    }

    public Lot getLotById(long lot_id) throws Exception {
        String query = "SELECT * FROM Lots WHERE lot_id = ?";
            Map<String, Object> resultMap = jdbcTemplate.queryForMap(query,lot_id);
            if (resultMap.isEmpty()){
                throw new Exception("there is no lot with this id");
            }
            resultMap.put("num_of_spots" , spotDAO.getNumOfAvailableSpots((Long) resultMap.get("lot_id")));
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
    public double getLotFee(long lot_id){
        String query = "SELECT fee FROM Lots WHERE lot_id = ?";
        return jdbcTemplate.queryForObject(query, Double.class, lot_id);
    }
    public void updateLotRevenue(long price , long lotId) {
        String query = "UPDATE Lots SET revenue = revenue + ? WHERE lot_id = ? ";
        int rows  = jdbcTemplate.update(query, price, lotId);
        if(rows == 0){
            throw new RuntimeException("error updating revenue");
        }
    }
    public List<Map<String , Object>> getTopLots(){
        String query = "SELECT lot_id , revenue From Lots ORDER BY revenue DESC LIMIT 20 ";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        return rows;
    }

    public long getLotManagerIdByLotId(long lotId){
        String query = "SELECT manager FROM Lots WHERE lot_id = ?";
        return jdbcTemplate.queryForObject(query, Long.class, lotId);
    }

    public long getLotRevenue(long lotId){
        String query = "SELECT revenue FROM Lots WHERE lot_id = ?";
        return jdbcTemplate.queryForObject(query, Long.class, lotId);
    }
}
