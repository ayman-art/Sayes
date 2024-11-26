package com.utopia.Sayes.Modules.Login.Handler;

import com.utopia.Sayes.Modules.Login.Handler.Exceptions.LoginException;
import com.utopia.Sayes.Modules.Login.Handler.Exceptions.PasswordException;

public class PasswordLoginHandler implements ILoginHandler {
    private ILoginHandler nextHandler;

    @Override
    public void handle(String username, String password) throws LoginException {
        if (!password.matches("^[A-Za-z\\d]{8,}$")) {
            throw new PasswordException("Password must be at least 8 characters long");
        }
        if (!password.matches("^(?=.*[A-Z])$")) {
            throw new PasswordException("Password must contain at least one uppercase letter");
        }
        if (!password.matches("^(?=.*[a-z])$")) {
            throw new PasswordException("Password must contain at least one lowercase letter");
        }
        if (!password.matches("^(?=.*[0-9])$")) {
            throw new PasswordException("Password must contain at least one digit");
        }
    }

    @Override
    public void setNextHandler(ILoginHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public ILoginHandler getNextHandler() {
        return nextHandler;
    }
}
