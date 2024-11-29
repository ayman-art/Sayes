package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Models.Admin;
import com.utopia.Sayes.Models.Driver;
import com.utopia.Sayes.Models.LotManager;
import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.SignUp.SignUpService;
import com.utopia.Sayes.enums.Role;
import com.utopia.Sayes.Models.User;

public class RegistrationFacade {
    public static String RegisterDriver(String name, String password, String plate, int license){
        Driver driver = SignUpService.registerDriver(name, password, plate, license);
        return Authentication.generateJWT(driver.getDriver_id(), name, Role.DRIVER);
    }

    public static String RegisterManager(String name, String password){
        LotManager manager = SignUpService.registerManager(name, password);
        return Authentication.generateJWT(manager.getManager_id(), name, Role.MANAGER);

    }
    public static String loginUser(String name, String password){
        User user = SignUpService.loginUser(name, password);
        if(user.getClass()== Admin.class)
            return Authentication.generateJWT(user.getUser_id(), name, Role.ADMIN);
        else if (user.getClass()== LotManager.class)
            return Authentication.generateJWT(user.getUser_id(), name, Role.MANAGER);
        else
            return Authentication.generateJWT(user.getUser_id(), name, Role.DRIVER);

    }

}
