package com.nafaz.android.entity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MobileInfo {

    private static String mobileNumber;

    public static void setMobileNumber(String mobileNum) {
        mobileNumber = mobileNum;
    }

    @Contract(pure = true)
    @NotNull
    public static String getMobileNumber() {
        return mobileNumber;
    }
}
