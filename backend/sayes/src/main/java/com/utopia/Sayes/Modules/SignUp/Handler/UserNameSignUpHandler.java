package com.utopia.Sayes.Modules.SignUp.Handler;

import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.UsernameException;
import com.utopia.Sayes.Repo.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class UserNameSignUpHandler implements ISignUpHandler{
    private ISignUpHandler nextHandler;
    @Autowired
    UserDAO userDAO = new UserDAO();
    @Override
    public void handle(Map<String, Object> data) throws UsernameException {

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
