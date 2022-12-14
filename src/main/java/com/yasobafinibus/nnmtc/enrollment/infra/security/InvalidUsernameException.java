package com.yasobafinibus.nnmtc.enrollment.infra.security;

public class InvalidUsernameException extends RuntimeException {

    public InvalidUsernameException(String message) {
        super(message + " is invalid");
    }

}
