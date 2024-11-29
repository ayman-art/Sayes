package com.utopia.Sayes.Modules.Login.Handler.Exceptions;

public class NullOrEmptyUserNameException extends LoginException {
    public NullOrEmptyUserNameException(String message) {
        super(message);
    }
}
