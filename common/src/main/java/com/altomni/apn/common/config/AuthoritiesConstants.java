package com.altomni.apn.common.config;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    /**
     * super admin for the platform, only for yisu
     */
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String TENANT_ADMIN = "ROLE_TENANT_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String PRIMARY_RECRUITER = "ROLE_PRIMARY_RECRUITER";

    public static final String ACCOUNT_MANAGER = "ROLE_ACCOUNT_MANAGER";

    public static final String SALES = "ROLE_SALES";

    public static final String LIMIT_USER = "ROLE_LIMIT_USER";

    public static final String CONSUMER = "ROLE_CONSUMER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String TRIAL_USER = "ROLE_TRIAL_USER";

    public static final String HR = "ROLE_HR";

    public static final String ES = "ROLE_ES";

    public static final String TENANT_ADMIN_PUBLIC = "ROLE_TENANT_ADMIN_PUBLIC";

    public static final String CONTRACT = "ROLE_CONTRACT";

    public static final String PRIVILEGE_REPORT = "PRIVILEGE_REPORT";

    private AuthoritiesConstants() {
    }
}
