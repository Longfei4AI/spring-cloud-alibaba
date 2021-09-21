package com.altomni.apn.authority.exception;

import com.altomni.apn.common.errors.BadRequestAlertException;
import com.altomni.apn.authority.config.ErrorConstants;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
