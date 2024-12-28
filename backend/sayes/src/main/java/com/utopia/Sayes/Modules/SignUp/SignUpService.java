package com.utopia.Sayes.Modules.SignUp;

import com.utopia.Sayes.Models.Driver;
import com.utopia.Sayes.Models.LotManager;
import com.utopia.Sayes.Models.User;
import com.utopia.Sayes.Modules.Login.LoginValidation;
import com.utopia.Sayes.Repo.DriverDAO;
import com.utopia.Sayes.Repo.LotManagerDAO;
import com.utopia.Sayes.Repo.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;

@Service
public class SignUpService {
    @Autowired
    UserDAO user_dao;
    @Autowired
    DriverDAO driver_dao;
    @Autowired
    LotManagerDAO manager_dao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Driver registerDriver(String name, String password, String plate, int license) {
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL RegisterDriver(?, ?, ?, ?, ?)}")) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, plate);
            stmt.setInt(4, license);
            stmt.registerOutParameter(5, Types.BIGINT);

            stmt.execute();

            long driverId = stmt.getLong(5);
            System.out.println(driverId);
            Driver d = new Driver(name, password, 0, plate, license);
            d.setUser_id(driverId);
            return d;

        } catch (SQLException e) {
            throw new RuntimeException("Error registering driver: " + e.getMessage(), e);
        }
    }

    public LotManager registerManager(String name, String password) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL RegisterManager(?, ?, ?)}")) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.BIGINT);

            stmt.execute();

            long managerId = stmt.getLong(3);
            LotManager m = new LotManager(name, password, 0);
            m.setUser_id(managerId);
            return m;

        } catch (SQLException e) {
            throw new RuntimeException("Error registering manager: " + e.getMessage(), e);
        }
    }

    public User loginUser(String name, String password) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL LoginUser(?, ?, ?)}")) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.BOOLEAN);

            stmt.execute();

            boolean isAuthenticated = stmt.getBoolean(3);

            if (isAuthenticated) {
                return user_dao.getUser(name);
            } else {
                throw new RuntimeException("Incorrect username or password");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error logging in user: " + e.getMessage(), e);
        }
    }

}
