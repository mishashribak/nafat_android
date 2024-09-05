package com.nafaz.android.api.entity;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CheckOtpRequest {

    private int otp;
    private BigInteger personId;
}
