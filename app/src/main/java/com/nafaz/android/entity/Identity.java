package com.nafaz.android.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class Identity {

    public static String id;

    public static void setPersonId(String personId) {
        id = personId;
    }

    @NotNull
    public static BigInteger getPersonId() {
        return BigInteger.valueOf(Long.parseLong(id));
    }
}
