package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.LotAdapter;
import com.utopia.Sayes.Models.Lot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LotDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LotAdapter lotAdapter = new LotAdapter();

    public void addLot(Lot lot){
        String query = "INSERT INTO Lots " +
                " (manager, location, price, num_of_spots, details) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query , lot.getManager_id(), lot.getLocation(), lot.getPrice(),
                    lot.getNum_of_spots(), lot.getDetails());
        }
        catch (Exception e){
            System.out.println("Error while adding lot: " + e);
        }
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
    public void decrementAvailableSpots(int lotId) {
        String query = "UPDATE Lots SET num_of_spots = num_of_spots - 1 WHERE id = ? AND num_of_spots > 0";
        int rowsUpdated = jdbcTemplate.update(query, lotId);
        if (rowsUpdated == 0) {
            throw new IllegalStateException("No available spots: " + lotId);
        }
    }
    public void incrementAvailableSpots(int lotId) {
        String query = "UPDATE Lots SET num_of_spots = num_of_spots + 1 WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(query, lotId);
        if (rowsUpdated == 0) {
            throw new IllegalStateException("Can't increment spots fro this manager: " + lotId);
        }
    }

    public void getLotById(long lot_id){
        String query = "SELECT * FROM Lots WHERE lot_id = ?";
        try {
            jdbcTemplate.update(query,lot_id);
        } catch (Exception e) {
            System.out.println("Error while Selecting lot : " + e);
        }
    }
}
