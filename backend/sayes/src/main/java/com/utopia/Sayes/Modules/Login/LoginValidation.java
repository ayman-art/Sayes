package com.utopia.Sayes.Modules.Login;

import com.utopia.Sayes.Modules.Login.Handler.Exceptions.LoginException;
import com.utopia.Sayes.Modules.Login.Handler.ILoginHandler;
import com.utopia.Sayes.Modules.Login.Handler.PasswordLoginHandler;
import com.utopia.Sayes.Modules.Login.Handler.UserHandler;

import java.util.logging.Handler;

public class LoginValidation {
    private final String username;
    private final String password;

    public LoginValidation(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void validate() throws LoginException {
        ILoginHandler handlerChain = generateHandlerChain();
        while (handlerChain != null) {
            handlerChain.handle(username, password);
            handlerChain = handlerChain.getNextHandler();
        }
    }

    private ILoginHandler generateHandlerChain() {
        ILoginHandler handler = new PasswordLoginHandler();
        handler.setNextHandler(new UserHandler());
        return handler;
    }
}

