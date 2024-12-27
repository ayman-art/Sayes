package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.LotManagerAdapter;
import com.utopia.Sayes.Models.LotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class LotManagerDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    LotDAO lotDAO;

    LotManagerAdapter lotManagerAdapter = new LotManagerAdapter();

    public void addLotManager(LotManager lotManager) {
        String query = "INSERT INTO lot_managers (manager_id, revenue) VALUES (?, ?)";
        jdbcTemplate.update(query, lotManager.getUser_id(), lotManager.getRevenue());
    }
    public boolean doesManagerExist(long managerId) {
        String query = "SELECT EXISTS(SELECT 1 FROM lot_managers WHERE manager_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, managerId));
    }
    public LotManager getManagerById(long managerId) throws Exception {
        String query = """
                SELECT * FROM Users
                LEFT JOIN lot_managers ON Users.user_id = lot_managers.manager_id
                WHERE user_id = ?
                """;
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, managerId);
        resultMap.remove("revenue");
        long newRevenue = lotDAO.getTotalRevenue(managerId);
        resultMap.put("revenue" , newRevenue);
        if(resultMap.isEmpty()){
            throw new Exception("error");
        }
        return lotManagerAdapter.fromMap(resultMap);
    }
}
