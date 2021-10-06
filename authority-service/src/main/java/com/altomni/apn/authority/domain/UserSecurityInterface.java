package com.altomni.apn.authority.domain;

import com.altomni.apn.authority.domain.enumeration.AccountType;

import java.util.Set;

/**
 * @author longfeiwang
 */
public interface UserSecurityInterface {

    Long getId();

    AccountType getAccountType();

    boolean isActivated();

    Set<Authority> getAuthorities();

    String getPassword();
}
