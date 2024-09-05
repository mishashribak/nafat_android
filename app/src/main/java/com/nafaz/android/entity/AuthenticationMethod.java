package com.nafaz.android.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nafaz.android.util.Utils;

import java.util.List;

public class AuthenticationMethod {

    @SerializedName("allowed_steps_one")
    @Expose
    public List<AllowedStepsOne> arrayOfStepsOne = null;
    @SerializedName("allowed_steps_two")
    @Expose
    public List<AllowedStepsTwo> arrayOfStepsTwo = null;

    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("status")
    @Expose
    public Integer status; // status = 2 not active ----- status = 1 active
    @SerializedName("name")
    @Expose
    public String name;

    public boolean isActive() {
        return !Utils.isNullOrEmpty(status) && status == 1;
    }

    public boolean isArrayOfStepsOneEmpty() {
        return arrayOfStepsOne == null || arrayOfStepsOne.isEmpty();
    }

    public boolean isArrayOfStepsTwoEmpty() {
        return arrayOfStepsTwo == null || arrayOfStepsTwo.isEmpty();
    }

    public boolean isArrayOfStepsOneHasOneItem() {
        return !isArrayOfStepsOneEmpty() && arrayOfStepsOne.size() == 1;
    }

    public boolean isArrayOfStepsTwoHasOneItem() {
        return !isArrayOfStepsTwoEmpty() && arrayOfStepsTwo.size() == 1;
    }

    public boolean isResponseHaveOneStep() {
        return isArrayOfStepsOneHasOneItem() || isArrayOfStepsTwoHasOneItem();
    }

    public boolean isResponseHaveEmptySteps() {
        return isArrayOfStepsOneEmpty() && isArrayOfStepsTwoEmpty();
    }
}
