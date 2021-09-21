package com.altomni.apn.authority.exception;

import com.altomni.apn.common.errors.BadRequestAlertException;
import com.altomni.apn.authority.config.ErrorConstants;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists");
    }
}
