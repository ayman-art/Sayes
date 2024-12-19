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
        String query = "INSERT INTO lot_managers (manager_id, revenue) VALUES (?, ?)";
        jdbcTemplate.update(query, lotManager.getUser_id(), lotManager.getRevenue());
    }
    public boolean doesManagerExist(long managerId) {
        String query = "SELECT EXISTS(SELECT 1 FROM lot_managers WHERE manager_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, managerId));
    }
}
