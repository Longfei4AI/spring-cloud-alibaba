package com.altomni.apn.common.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public BadRequestAlertException(String defaultMessage) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, null, null);
    }

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, "Bad Request", Status.BAD_REQUEST, null, null, null, getAlertParameters(defaultMessage));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
    private static Map<String, Object> getAlertParameters(String defaultMessage) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", defaultMessage);
        //parameters.put("params", entityName);
        return parameters;
    }
}
