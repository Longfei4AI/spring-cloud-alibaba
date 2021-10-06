package com.altomni.apn.authority.domain.enumeration;

import com.altomni.apn.common.enumeration.support.AbstractAttributeConverter;

import javax.persistence.Converter;

@Converter
public class AccountTypeConverter extends AbstractAttributeConverter<AccountType, Integer> {
    public AccountTypeConverter() {
        super(AccountType::toDbValue, AccountType::fromDbValue);
    }
}
