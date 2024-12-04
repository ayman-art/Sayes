package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Adapters.AdminAdapter;
import com.utopia.Sayes.Adapters.DriverAdapter;
import com.utopia.Sayes.Adapters.LotManagerAdapter;
import com.utopia.Sayes.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) {
        String query = "INSERT INTO Users (username, user_password) VALUES (?, ?)";
        jdbcTemplate.update(query, user.getUsername(), user.getUser_password());
    }
    public long getUserId(String username) throws NullPointerException{
        String query = "SELECT user_id FROM Users WHERE username = ?";
        Long id = jdbcTemplate.queryForObject(query, Long.class, username);
        if(id == null) throw new NullPointerException();
        return id;
    }

    public boolean doesUsernameExist(String username) {
        String query = "SELECT EXISTS(SELECT 1 FROM Users WHERE username = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, username));
    }

    public boolean doesUserExist(String username, String password) {
        String query = "SELECT EXISTS(SELECT 1 FROM Users WHERE username = ? AND user_password = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, username, password));
    }

    public User getUser(String username) {
        String query = """
                SELECT * FROM Users
                LEFT JOIN lot_managers ON Users.user_id = lot_managers.manager_id
                LEFT JOIN Admins ON Users.user_id = Admins.Admin_id
                LEFT JOIN Drivers ON Users.user_id = Drivers.Driver_id
                WHERE username = ?
                """;
        Map<String, Object> user = jdbcTemplate.queryForMap(query, username);
        if (user.get("Admin_id") != null) {
            AdminAdapter adminAdapter = new AdminAdapter();
            return adminAdapter.fromMap(user);
        } else if (user.get("Manager_id") != null) {
            LotManagerAdapter lotManagerAdapter = new LotManagerAdapter();
            return lotManagerAdapter.fromMap(user);
        } else if (user.get("Driver_id") != null) {
            System.out.println("here");
            DriverAdapter driverAdapter = new DriverAdapter();
            return driverAdapter.fromMap(user);
        }
        return null;
    }
}
