package com.utopia.Sayes.Modules.SignUp.Handler;

import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.PasswordException;

import java.util.Map;

public class PasswordSignUpHandler implements ISignUpHandler {
    private ISignUpHandler nextHandler;

    @Override
    public void handle(Map<String, Object> data) throws PasswordException {
        String password = data.get("password").toString();
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
    public void setNextHandler(ISignUpHandler handler) {
        this.nextHandler = handler;
    }

    @Override
    public ISignUpHandler getNextHandler() {
        return this.nextHandler;
    }
}
