package com.nafaz.android.api.entity;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CheckTOTPRequest {
    private BigInteger personId;
    private String totp;
}
