package com.utopia.Sayes.Modules.SignUp;

import com.utopia.Sayes.Adapters.AdminAdapter;
import com.utopia.Sayes.Adapters.DriverAdapter;
import com.utopia.Sayes.Adapters.LotManagerAdapter;
import com.utopia.Sayes.Models.Admin;
import com.utopia.Sayes.Models.Driver;
import com.utopia.Sayes.Models.User;

import java.util.Map;

public class UserFactory {
    public static User getUser(String userType, Map<String, Object> data) {
        switch (userType) {
            case "Driver" -> {
                DriverAdapter driverAdapter = new DriverAdapter();
                return driverAdapter.fromMap(data);
            }
            case "Admin" -> {
                AdminAdapter adminAdapter = new AdminAdapter();
                return adminAdapter.fromMap(data);
            }
            case "LotManager" -> {
                LotManagerAdapter lotManagerAdapter = new LotManagerAdapter();
                return lotManagerAdapter.fromMap(data);
            }
            default -> {
                return null;
            }
        }
    }
}
