package com.utopia.Sayes.Modules.Login.Handler;

import com.utopia.Sayes.Modules.Login.Handler.Exceptions.LoginException;

public interface ILoginHandler {
    void handle(String username, String password) throws LoginException;
    void setNextHandler(ILoginHandler handler);
    ILoginHandler getNextHandler();
}
