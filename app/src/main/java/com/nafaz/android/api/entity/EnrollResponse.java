package com.nafaz.android.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class EnrollResponse {
    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("messageAr")
    @Expose
    public String messageAr;
    @SerializedName("tOtpUrl")
    @Expose
    public String tOtpUrl;

    @SerializedName("avaliableActions")
    @Expose
    public List<String> availableActions;

}
