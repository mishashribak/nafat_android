package com.nafaz.android.api.entity;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CheckMobileRequest {

    private String mobileNumber;
    private BigInteger personId;
    private String lang;
}
