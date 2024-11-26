package com.utopia.Sayes.Modules.SignUp;

import com.utopia.Sayes.Models.User;
import com.utopia.Sayes.Modules.SignUp.Handler.DataFormatSignUpHandler;
import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.SignUpException;
import com.utopia.Sayes.Modules.SignUp.Handler.ISignUpHandler;
import com.utopia.Sayes.Modules.SignUp.Handler.LicenseNumberSignUpHandler;
import com.utopia.Sayes.Modules.SignUp.Handler.PlateNumberSignUpHandler;

import java.util.Map;

public class SignUpValidation {
    private final String userType;
    private final Map<String, Object> data;

    SignUpValidation(String userType, Map<String, Object> data) {
        this.userType = userType;
        this.data = data;
    }

    public User validate() throws SignUpException {
        ISignUpHandler handler = generateHandlerChain();
        while (handler != null) {
            handler.handle(this.data);
            handler = handler.getNextHandler();
        }
        return UserFactory.getUser(this.userType, this.data);
    }

    private ISignUpHandler generateHandlerChain() {
        ISignUpHandler handler = new DataFormatSignUpHandler();
        handler.setNextHandler(new PlateNumberSignUpHandler());
        if (this.userType.equals("Driver")) {
            handler.getNextHandler().setNextHandler(new LicenseNumberSignUpHandler());
            handler.getNextHandler().getNextHandler().setNextHandler(new PlateNumberSignUpHandler());
        }
        return handler;
    }
}
