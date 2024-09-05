package com.nafaz.android.api.entity;

import java.math.BigInteger;

public class RegisterMobileRequest extends CheckMobileRequest {

    public RegisterMobileRequest(String mobileNumber, BigInteger personId, String lang) {
        super(mobileNumber, personId, lang);
    }
}
