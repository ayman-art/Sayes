package com.utopia.Sayes.Modules.SignUp.Handler;

import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.LicenseNumberException;
import com.utopia.Sayes.Repo.DriverDAO;

import java.util.Map;
import java.util.Objects;

public class LicenseNumberSignUpHandler implements ISignUpHandler {
    private ISignUpHandler nextHandler;

    @Override
    public void handle(Map<String, Object> data) throws LicenseNumberException {
        DriverDAO driverDAO = new DriverDAO();
        if (driverDAO.doesLicenseNumberExist((Long) Objects.requireNonNull(data.get("license_number")))) {
            throw new LicenseNumberException("License number already exists");
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
