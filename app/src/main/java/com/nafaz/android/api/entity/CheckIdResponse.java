package com.nafaz.android.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nafaz.android.entity.AuthenticationMethod;

import java.util.List;

public class CheckIdResponse {

    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("messageAr")
    @Expose
    public String messageAr;
    @SerializedName("authMethods")
    @Expose
    public AuthenticationMethod authenticationMethods;

    @SerializedName("avaliableActions")
    @Expose
    public List<String> availableActions;
}
