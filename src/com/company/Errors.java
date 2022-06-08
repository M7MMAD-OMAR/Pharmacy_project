package com.company;

public class Errors {

    public void errorLength(String value, String mainValue, String errorMessageInZero, String errorMessageInMin, String errorMessageInMax, int valueMaxNumber) throws error_length {
        if (value.length() != 0) {
            if (value.length() > 2) {
                if (value.length() < valueMaxNumber) {
                    mainValue = value;
                } else {
                    throw new error_length(errorMessageInMax);
                }
            } else {
                throw new error_length(errorMessageInMin);
            }
        } else {
            throw new error_length(errorMessageInZero);
        }

    }
}

class error_length extends Exception {
    public error_length(String msg) {
        super(msg);
    }
}


