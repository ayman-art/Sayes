package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addAdmin(Admin admin) {
        String query = "INSERT INTO Admins (Admin_id) VALUES (?)";
        jdbcTemplate.update(query, admin.getAdmin_id());
    }
}
