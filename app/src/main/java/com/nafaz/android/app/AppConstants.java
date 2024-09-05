package com.nafaz.android.app;

public class AppConstants {

    public interface Extras {
        String EXTRA_TYPE = "type";
        String EXTRA_MOBILE_VER_MODE = "mobile_ver_mode";
        String EXTRA_MOBILE_VER_NORMAL = "mobile_ver_normal";
        String EXTRA_MOBILE_VER_TOTP = "mobile_ver_totp";
        String EXTRA_MOBILE_VER_NEW = "mobile_ver_new";

        String EXTRA_SUCCESS_MODE = "success_mode";
        String EXTRA_SUCCESS_TOTP = "success_totp";
        String EXTRA_SUCCESS_USERNAME = "success_username";
        String EXTRA_SUCCESS_FACE = "success_face";
        String EXTRA_SUCCESS_MOBILE = "success_mobile";
        String EXTRA_SUCCESS_REGISTRATION = "success_registration";
    }

    public interface ValidationRules {
        int FIELD_ID_LENGTH = 10;
        int FIELD_USERNAME_MIN_LENGTH = 3;
        int FIELD_PASSWORD_LENGTH = 4;
        int FIELD_OTP_LENGTH = 4;
        String COUNTRY_CODE_NAME_KSA = "SA";
        String COUNTRY_CODE_KSA = "966";
    }
}
