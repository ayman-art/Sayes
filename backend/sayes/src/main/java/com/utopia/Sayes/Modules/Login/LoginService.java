package com.utopia.Sayes.Modules.Login;

import com.utopia.Sayes.Models.User;
import com.utopia.Sayes.Modules.Login.Handler.Exceptions.LoginException;
import com.utopia.Sayes.Repo.UserDAO;

public class LoginService {
    public static User login(String username, String password) throws LoginException {
        try {
            LoginValidation loginValidation = new LoginValidation(username, password);
            loginValidation.validate();
            UserDAO userDAO = new UserDAO();
            return userDAO.getUser(username);
        } catch (LoginException e) {
            return null;
        }
    }
}
