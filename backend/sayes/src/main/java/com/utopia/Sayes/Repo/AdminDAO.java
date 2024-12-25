package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.AdminAdapter;
import com.utopia.Sayes.Models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AdminDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    AdminAdapter adminAdapter = new AdminAdapter();

    public void addAdmin(Admin admin) {
        String query = "INSERT INTO Admins (Admin_id) VALUES (?)";
        jdbcTemplate.update(query, admin.getUser_id());
    }
    public Admin getAdminById(long adminId) throws Exception {
        String query = """
                SELECT * FROM Users
                LEFT JOIN Admins ON Users.user_id = Admins.Admin_id
                WHERE user_id = ?
                """;
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, adminId);
        if(resultMap.isEmpty()){
            throw new Exception("error");
        }
        return adminAdapter.fromMap(resultMap);
    }
}
