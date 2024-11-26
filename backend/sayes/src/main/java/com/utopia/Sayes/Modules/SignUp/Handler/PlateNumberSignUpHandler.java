package com.utopia.Sayes.Modules.SignUp.Handler;

import com.utopia.Sayes.Models.User;
import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.PlateNumberException;
import com.utopia.Sayes.Repo.DriverDAO;

import java.util.Map;
import java.util.Objects;

public class PlateNumberSignUpHandler implements ISignUpHandler {
    private ISignUpHandler nextHandler;

    @Override
    public void handle(Map<String, Object> data) throws PlateNumberException {
        DriverDAO driverDAO = new DriverDAO();
        if (driverDAO.doesPlateNumberExist((String) Objects.requireNonNull(data.get("plate_number")))) {
            throw new PlateNumberException("Plate number already exists");
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
