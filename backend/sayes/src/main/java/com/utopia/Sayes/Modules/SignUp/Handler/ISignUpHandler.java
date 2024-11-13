package com.utopia.Sayes.Modules.SignUp.Handler;

import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.SignUpException;

import java.util.Map;

public interface ISignUpHandler {
    void handle(Map<String, Object> data) throws SignUpException;

    void setNextHandler(ISignUpHandler handler);

    ISignUpHandler getNextHandler();
}
