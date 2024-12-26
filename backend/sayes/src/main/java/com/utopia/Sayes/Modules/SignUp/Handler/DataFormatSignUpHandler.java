package com.utopia.Sayes.Modules.SignUp.Handler;

import com.utopia.Sayes.Modules.SignUp.Handler.Exceptions.DataFormatException;

import java.util.Map;

public class DataFormatSignUpHandler implements ISignUpHandler {
    private ISignUpHandler nextHandler;

    @Override
    public void handle(Map<String, Object> data) throws DataFormatException {
        if (!isValidLong(data.get("licence_number"))) {
            throw new DataFormatException("Invalid licence number");
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

    private boolean isValidLong(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Number) {
            long value = ((Number) obj).longValue();
            if (obj instanceof Double || obj instanceof Float) {
                double originalValue = ((Number) obj).doubleValue();
                return originalValue == (double) value;
            }
            return true;
        }

        try {
            Long.parseLong(obj.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
