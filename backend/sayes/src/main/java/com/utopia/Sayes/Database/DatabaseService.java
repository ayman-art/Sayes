package com.utopia.Sayes.Database;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        createTables();
    }

    private void createTables() {
        try {
            // create user table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(255)  UNIQUE NOT NULL, " +
                    "user_password VARCHAR(255) NOT NULL)";

            //create lot managers table
            String createLotManagersTable = "CREATE TABLE IF NOT EXISTS lot_managers (" +
                    "manager_id BIGINT NOT NULL PRIMARY KEY, " +
                    "revenue BIGINT NOT NULL, " +
                    "FOREIGN KEY (manager_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE)";

            //create Admins table
            String createAdminsTable = "CREATE TABLE IF NOT EXISTS Admins (" +
                    "Admin_id BIGINT NOT NULL PRIMARY KEY, " +
                    "FOREIGN KEY (Admin_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE)";

            //create drivers table
            String createDriversTable = "CREATE TABLE IF NOT EXISTS Drivers (" +
                    "Driver_id BIGINT NOT NULL PRIMARY KEY, " +
                    "plate_number VARCHAR(255) UNIQUE NOT NULL, " +
                    "balance BIGINT NOT NULL, " +
                    "payment_method VARCHAR(255), " +
                    "license_number BIGINT NOT NULL, " +
                    "FOREIGN KEY (Driver_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE)";

            //create lots table
            String createLotsTable = "CREATE TABLE IF NOT EXISTS Lots (" +
                    "lot_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "manager BIGINT NOT NULL, " +
                    "longitude DOUBLE NOT NULL, " +
                    "latitude DOUBLE NOT NULL, " +
                    "revenue BIGINT NOT NULL, " +
                    "price DOUBLE (15 , 2) NOT NULL, " +
                    "lot_type VARCHAR(255) NOT NULL,"+
                    "penalty DOUBLE (15 , 2) NOT NULL, " +
                    "fee DOUBLE (15 , 2) NOT NULL, " +
                    "time TIME NOT NULL, " +
                    "details VARCHAR(255), " +
                    "FOREIGN KEY (manager) REFERENCES lot_managers (manager_id) ON DELETE CASCADE ON UPDATE CASCADE)";

            //create spots table
            String createSpotsTable = "CREATE TABLE IF NOT EXISTS spots (" +
                    "spot_id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "lot_id BIGINT NOT NULL, " +
                    "state VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (spot_id, lot_id), " +
                    "FOREIGN KEY (lot_id) REFERENCES Lots (lot_id) ON DELETE CASCADE ON UPDATE CASCADE)";

            //reserved spots table
            String createReservedSpotsTable = "CREATE TABLE IF NOT EXISTS reserved_spots (" +
                    "spot_id BIGINT NOT NULL, " +
                    "lot_id BIGINT NOT NULL, " +
                    "start_time DATETIME NOT NULL, " +
                    "end_time DATETIME NOT NULL, " +
                    "state VARCHAR(255), " +
                    "driver_id BIGINT NOT NULL, " +
                    "price DOUBLE (15 , 2) NOT NULL, " +
                    "PRIMARY KEY (lot_id, spot_id), " +
                    "FOREIGN KEY (lot_id) REFERENCES Lots (lot_id), " +
                    "FOREIGN KEY (spot_id) REFERENCES spots (spot_id), " +
                    "FOREIGN KEY (driver_id) REFERENCES Drivers (Driver_id) ON DELETE CASCADE ON UPDATE CASCADE)";

            //logs table
            String createLogsTable = "CREATE TABLE IF NOT EXISTS logs (" +
                    "driver_id BIGINT NOT NULL, " +
                    "reservation_time DATETIME NOT NULL, " +
                    "departure_time DATETIME NOT NULL, " +
                    "spot_id BIGINT NOT NULL, " +
                    "lot_id BIGINT NOT NULL, " +
                    "FOREIGN KEY (lot_id) REFERENCES Lots (lot_id), " +
                    "FOREIGN KEY (spot_id) REFERENCES spots (spot_id), " +
                    "FOREIGN KEY (driver_id) REFERENCES Drivers (Driver_id) ON DELETE CASCADE ON UPDATE CASCADE)";

            //execute queries
            jdbcTemplate.execute(createUsersTable);
            jdbcTemplate.execute(createLotManagersTable);
            jdbcTemplate.execute(createAdminsTable);
            jdbcTemplate.execute(createDriversTable);
            jdbcTemplate.execute(createLotsTable);
            jdbcTemplate.execute(createSpotsTable);
            jdbcTemplate.execute(createReservedSpotsTable);
            jdbcTemplate.execute(createLogsTable);

            System.out.println("Tables created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}