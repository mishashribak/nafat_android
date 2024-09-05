package com.nafaz.android.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nafaz.android.R;
import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.util.Utils;

import org.jetbrains.annotations.Contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {

    @SerializedName("arabicFamilyName")
    @Expose
    public String arabicFamilyName;
    @SerializedName("arabicFatherName")
    @Expose
    public String arabicFatherName;
    @SerializedName("arabicFirstName")
    @Expose
    public String arabicFirstName;
    @SerializedName("arabicGrandFatherName")
    @Expose
    public String arabicGrandFatherName;
    @SerializedName("arabicName")
    @Expose
    public String arabicName;
    @SerializedName("arabicNationality")
    @Expose
    public String arabicNationality;
    @SerializedName("birthDateG")
    @Expose
    public String birthDateG;
    @SerializedName("birthDateH")
    @Expose
    public String birthDateH;
    @SerializedName("deathFlag")
    @Expose
    public Boolean deathFlag;
    @SerializedName("englishFamilyName")
    @Expose
    public String englishFamilyName;
    @SerializedName("englishFatherName")
    @Expose
    public String englishFatherName;
    @SerializedName("englishFirstName")
    @Expose
    public String englishFirstName;
    @SerializedName("englishGrandFatherName")
    @Expose
    public String englishGrandFatherName;
    @SerializedName("englishName")
    @Expose
    public String englishName;
    @SerializedName("englishNationality")
    @Expose
    public String englishNationality;
    @SerializedName("finalExitFlag")
    @Expose
    public Boolean finalExitFlag;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("nationalityCode")
    @Expose
    public Integer nationalityCode;
    @SerializedName("nationalityWithdrawnFlag")
    @Expose
    public Boolean nationalityWithdrawnFlag;
    @SerializedName("suspensionFlag")
    @Expose
    public Boolean suspensionFlag;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("cardIssueDateG")
    @Expose
    public String cardIssueDateG;
    @SerializedName("cardIssueDateH")
    @Expose
    public String cardIssueDateH;
    @SerializedName("issueLocationAr")
    @Expose
    public String issueLocationAr;
    @SerializedName("issueLocationEn")
    @Expose
    public String issueLocationEn;
    @SerializedName("idExpiryDateG")
    @Expose
    public String idExpiryDateG;
    @SerializedName("idExpiryDateH")
    @Expose
    public String idExpiryDateH;
    @SerializedName("idVisrionNo")
    @Expose
    public Integer idVisrionNo;
    @SerializedName("travelStatus")
    @Expose
    public String travelStatus;
    @SerializedName("idtype")
    @Expose
    public String idtype;
    @SerializedName("iqamaExpiryDateG")
    @Expose
    public String iqamaExpiryDateG;
    @SerializedName("iqamaExpiryDateH")
    @Expose
    public String iqamaExpiryDateH;

    private static UserProfile userProfile;

    public static void set(UserProfile profile) {
        userProfile = profile;
    }

    @Contract(pure = true)
    public static UserProfile get() {
        return userProfile;
    }

    public String getGender() {
        if (NafazConfig.isEnglish())
            if (gender.contains("F"))
                return Utils.getString(R.string.label_gender_female);
            else
                return Utils.getString(R.string.label_gender_male);

        return "";
    }

    public String getFirstName() {
        return NafazConfig.isEnglish() ? englishFirstName : arabicFirstName;
    }

    public String getFatherName() {
        return NafazConfig.isEnglish() ? englishFatherName: arabicFatherName;
    }

    public String getFamilyName() {
        return NafazConfig.isEnglish() ? englishFamilyName: arabicFamilyName;
    }

    public String getGrandFatherName() {
        return NafazConfig.isEnglish() ? englishGrandFatherName: arabicGrandFatherName;
    }

    public String getName() {
        return NafazConfig.isEnglish() ? englishName: arabicName;
    }

    public String getBirthDate() {
        return NafazConfig.isEnglish() ? birthDateG: birthDateH;
    }

    public String getNationality() {
        return NafazConfig.isEnglish() ? englishNationality: arabicNationality;
    }

    public String getCardIssueDate() {
        return NafazConfig.isEnglish() ? cardIssueDateG: cardIssueDateH;
    }

    public String getIssueLocation() {
        return NafazConfig.isEnglish() ? issueLocationEn: issueLocationAr;
    }

    public String getIdExpiryDate() {
        return NafazConfig.isEnglish() ? idExpiryDateG: idExpiryDateH;
    }

    public String getIqamaExpiryDate() {
        return NafazConfig.isEnglish() ? iqamaExpiryDateG: iqamaExpiryDateH;
    }
}
