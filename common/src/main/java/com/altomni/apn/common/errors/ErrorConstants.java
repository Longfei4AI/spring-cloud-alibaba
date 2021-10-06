package com.altomni.apn.common.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = null; //"https://www.altomni.com/problem";
    public static final URI DEFAULT_TYPE = null; //URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = null; //URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = null; //URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = null; //URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = null; //URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI PARAMETERIZED_TYPE = null; //URI.create(PROBLEM_BASE_URL + "/parameterized");

    private ErrorConstants() {}
}
