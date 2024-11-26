package com.utopia.Sayes.Repo;

import com.utopia.Sayes.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) {
        String query = "INSERT INTO Users (user_id, username, user_password) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, user.getUser_id(), user.getUsername(), user.getUser_password());
    }

    public boolean doesUsernameExist(String username) {
        String query = "SELECT EXISTS(SELECT 1 FROM Users WHERE username = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, username));
    }
}
