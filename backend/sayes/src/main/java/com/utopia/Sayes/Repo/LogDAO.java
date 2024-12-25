package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.LogAdapter;
import com.utopia.Sayes.Models.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class LogDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    LogAdapter logAdapter = new LogAdapter();
    public void addlog(long spot_id, long lot_id, Date start_time , Date end_time,  long driver_id) {
        String query = "INSERT INTO logs"  +
                "(driver_id, reservation_time, departure_time, spot_id , lot_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        int rows = jdbcTemplate.update(query, driver_id ,start_time,end_time,spot_id,lot_id);
        if(rows == 0){
            throw new RuntimeException("can't add this log");
        }
    }
    public List<Log> getlogs() throws Exception {
        String query = "SELECT * FROM logs";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        List<Log> logs = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Log log = logAdapter.fromMap(row);
            logs.add(log);
        }
        return logs;
    }
    public List<Map<String , Object>> getTopUsers(){
        String query = "SELECT u.username COUNT(l.driver_id) AS total_reservations " +
                "FROM logs l JOIN Users u " +
                "ON u.user_id = l.driver_id " +
                "GROUP BY l.driver_id , u.username " +
                "ORDER BY total_reservations DESC " +
                "LIMIT 20";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        return rows;
    }
}
