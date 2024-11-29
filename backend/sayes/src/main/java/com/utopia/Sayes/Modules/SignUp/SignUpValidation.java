package com.utopia.Sayes.Modules.SignUp;

import com.utopia.Sayes.Models.User;
import com.utopia.Sayes.Modules.SignUp.Handler.*;
import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.SignUpException;

import java.util.Map;

public class SignUpValidation {
    private final String userType;
    private final Map<String, Object> data;

    public SignUpValidation(String userType, Map<String, Object> data) {
        this.userType = userType;
        this.data = data;
    }

    public User validate() throws SignUpException {
        ISignUpHandler handler = generateHandlerChain();
        while (handler != null) {
            handler.handle(this.data);
            System.out.println("ay 7aga");
            handler = handler.getNextHandler();
        }
        return UserFactory.getUser(this.userType, this.data);
    }

    private ISignUpHandler generateHandlerChain() {
        ISignUpHandler handler = new PasswordSignUpHandler();
        if (this.userType.equals("Driver")) {
            handler.setNextHandler(new DataFormatSignUpHandler());
            handler.getNextHandler().setNextHandler(new PlateNumberSignUpHandler());
            handler.getNextHandler().getNextHandler().setNextHandler(new LicenseNumberSignUpHandler());
        }
        return handler;
    }
}
