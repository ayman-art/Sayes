package com.utopia.Sayes.Facades;

import com.utopia.Sayes.Models.Admin;
import com.utopia.Sayes.Models.Driver;
import com.utopia.Sayes.Models.LotManager;
import com.utopia.Sayes.Modules.Authentication;
import com.utopia.Sayes.Modules.SignUp.SignUpService;
import com.utopia.Sayes.enums.Role;
import com.utopia.Sayes.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationFacade {
    @Autowired
    SignUpService registration;
    public String RegisterDriver(String name, String password, String plate, int license){
        Driver driver = registration.registerDriver(name, password, plate, license);
        return Authentication.generateJWT(driver.getUser_id(), name, Role.DRIVER);
    }

    public String RegisterManager(String name, String password){

        LotManager manager = registration.registerManager(name, password);
        return Authentication.generateJWT(manager.getUser_id(), name, Role.MANAGER);

    }
    public String loginUser(String name, String password){
        User user = registration.loginUser(name, password);
        if(user.getClass()== Admin.class)
            return Authentication.generateJWT(user.getUser_id(), name, Role.ADMIN);
        else if (user.getClass()== LotManager.class)
            return Authentication.generateJWT(user.getUser_id(), name, Role.MANAGER);
        else
            return Authentication.generateJWT(user.getUser_id(), name, Role.DRIVER);

    }

}
