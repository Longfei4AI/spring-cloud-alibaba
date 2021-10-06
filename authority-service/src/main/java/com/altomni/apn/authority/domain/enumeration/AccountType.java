package com.altomni.apn.authority.domain.enumeration;


import com.altomni.apn.common.enumeration.support.ConvertedEnum;
import com.altomni.apn.common.enumeration.support.ReverseEnumResolver;

/**
 * The JobStatus enumeration.
 */
public enum AccountType implements ConvertedEnum<Integer> {

    USER(0),

    ADMIN(1);

    private final int dbValue;

    AccountType(int dbValue) {
        this.dbValue = dbValue;
    }

    @Override
    public Integer toDbValue() {
        return dbValue;
    }

    // static resolving:
    public static final ReverseEnumResolver<AccountType, Integer> resolver =
        new ReverseEnumResolver<>(AccountType.class, AccountType::toDbValue);

    public static AccountType fromDbValue(Integer dbValue) {
        return resolver.get(dbValue);
    }
}
