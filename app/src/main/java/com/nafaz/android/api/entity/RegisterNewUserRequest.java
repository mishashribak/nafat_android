package com.nafaz.android.api.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RegisterNewUserRequest {

    private String password;
    private String username;
    private String lang;
}
