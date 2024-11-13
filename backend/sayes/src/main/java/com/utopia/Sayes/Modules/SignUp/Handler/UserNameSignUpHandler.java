package com.utopia.Sayes.Modules.SignUp.Handler;

import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.UsernameException;
import com.utopia.Sayes.Repo.UserDAO;

import java.util.Map;

public class UserNameSignUpHandler implements ISignUpHandler{
    private ISignUpHandler nextHandler;
    @Override
    public void handle(Map<String, Object> data) throws UsernameException {
        UserDAO userDAO = new UserDAO();
        if (userDAO.doesUsernameExist((String) data.get("username"))) {
            throw new UsernameException("Username already exists");
        }
    }

    @Override
    public void setNextHandler(ISignUpHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public ISignUpHandler getNextHandler() {
        return this.nextHandler;
    }
}
