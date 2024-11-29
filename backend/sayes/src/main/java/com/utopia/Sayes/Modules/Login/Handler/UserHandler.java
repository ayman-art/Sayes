package com.utopia.Sayes.Modules.Login.Handler;

import com.utopia.Sayes.Modules.Login.Handler.Exceptions.LoginException;
import com.utopia.Sayes.Modules.Login.Handler.Exceptions.NullOrEmptyUserNameException;
import com.utopia.Sayes.Repo.UserDAO;

public class UserHandler implements ILoginHandler {
    private ILoginHandler nextHandler;

    @Override
    public void handle(String username, String password) throws LoginException {
        if (username == null || username.isEmpty()) {
            throw new NullOrEmptyUserNameException("Username cannot be empty");
        }
        UserDAO userDAO = new UserDAO();
        if (!userDAO.doesUserExist(username, password)) {
            throw new LoginException("User does not exist");
        }
    }

    @Override
    public void setNextHandler(ILoginHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public ILoginHandler getNextHandler() {
        return this.nextHandler;
    }
}
