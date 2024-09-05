package com.nafaz.android.api.entity;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CheckIdRequest {

    private String ipAddress;
    private BigInteger personId;
    private String deviceId;
}
