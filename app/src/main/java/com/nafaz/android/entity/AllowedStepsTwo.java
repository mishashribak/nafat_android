package com.nafaz.android.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllowedStepsTwo {

    @SerializedName("englishLabel")
    @Expose
    public String englishLabel;
    @SerializedName("arabicLabel")
    @Expose
    public String arabicLabel;
    @SerializedName("stepName")
    @Expose
    public String stepName;
}
