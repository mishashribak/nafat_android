package com.nafaz.android.api.helper;

import com.nafaz.android.api.entity.ApiResponse;
import com.nafaz.android.api.entity.CheckIdResponse;
import com.nafaz.android.app.NafazConfig;

public class ErrorParser {

    public static String getMessage(ApiResponse response) {
        return NafazConfig.isEnglish() ? response.message : response.messageAr;
    }

    public static String getMessage(CheckIdResponse response) {
        return NafazConfig.isEnglish() ? response.message : response.messageAr;
    }
}
