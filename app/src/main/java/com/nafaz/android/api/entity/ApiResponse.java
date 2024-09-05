package com.nafaz.android.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ApiResponse<T> {

    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("messageAr")
    @Expose
    public String messageAr;
    @SerializedName("data")
    @Expose
    public T data = null;

}