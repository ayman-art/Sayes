package com.utopia.Sayes.Modules.SignUp;

import com.utopia.Sayes.Models.Driver;
import com.utopia.Sayes.Models.LotManager;
import com.utopia.Sayes.Models.User;
import com.utopia.Sayes.Modules.Login.LoginValidation;
import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.SignUpException;
import com.utopia.Sayes.Repo.DriverDAO;
import com.utopia.Sayes.Repo.LotManagerDAO;
import com.utopia.Sayes.Repo.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class SignUpService {
    @Autowired
    UserDAO user_dao;
    @Autowired
    DriverDAO driver_dao;
    @Autowired
    LotManagerDAO manager_dao;
    public Driver registerDriver(String name, String password, String plate, int license){

        Map<String, Object> map = Map.of(
                "user_name", name,
                "user_password", password,
                "plate_number", plate,
                "license_number", license
        );
        SignUpValidation validator = new SignUpValidation("Driver", map);
        try {
            Driver user = (Driver) validator.validate();
            this.user_dao.addUser(user);
            long id = user_dao.getUserId(name);
            user.setDriver_id(id);
            this.driver_dao.addDriver(user);
            return user;
        } catch (SignUpException e) {
            throw new RuntimeException(e);
        }
    }
    public LotManager registerManager(String name, String password){
        Map<String, Object> map = Map.of(
                "user_name", name,
                "user_password", password
        );
        SignUpValidation validator = new SignUpValidation("LotManager", map);
        try {
            LotManager user = (LotManager) validator.validate();
            System.out.println("pass 2");
            user_dao.addUser(user);
            long id = user_dao.getUserId(name);
            user.setManager_id(id);
            manager_dao.addLotManager(user);
            return user;
        } catch (SignUpException e) {
            throw new RuntimeException(e);
        }
    }
    public static User loginUser(String name, String password){
        LoginValidation validator = new LoginValidation(name, password);
        try {
            validator.validate();
            UserDAO user_dao = new UserDAO();
            User user = user_dao.getUser(name);
            if (password.equals(user.getUser_password()))
                return user;
            else throw new Exception("Wrong password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
