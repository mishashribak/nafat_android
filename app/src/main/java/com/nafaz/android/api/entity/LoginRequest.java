package com.nafaz.android.api.entity;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginRequest {
    private String username;
    private String password;
    private String captcha;
}
