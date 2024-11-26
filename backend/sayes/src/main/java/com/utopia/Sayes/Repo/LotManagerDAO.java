package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Models.LotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LotManagerDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addLotManager(LotManager lotManager) {
        String query = "INSERT INTO lot_mangers (manager_id, revenue) VALUES (?, ?)";
        jdbcTemplate.update(query, lotManager.getManager_id(), lotManager.getRevenue());
    }
}
