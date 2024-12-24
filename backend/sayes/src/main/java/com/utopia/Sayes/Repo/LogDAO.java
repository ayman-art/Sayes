package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.LogAdapter;
import com.utopia.Sayes.Models.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

@Repository
public class LogDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    LogAdapter logAdapter = new LogAdapter();
    public void addlog(long spot_id, long lot_id, Date start_time , Date end_time, String state, long driver_id, double price) {
        String query = "INSERT INTO logs"  +
                "(driver_id, reservation_time, departure_time, spot_id , lot_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        int rows = jdbcTemplate.update(query, spot_id, lot_id, start_time , end_time, state, driver_id , price);
        if(rows == 0){
            throw new RuntimeException("can't add this log");
        }
    }
    public Log getlogs() throws Exception {
        String query = "SELECT * FROM logs";
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query);
        if(resultMap.isEmpty()){
            throw new Exception("There are no logs");
        }
        return logAdapter.fromMap(resultMap);
    }
}
