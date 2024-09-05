package com.nafaz.android.util;

import com.nafaz.android.app.AppConstants;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ValidationUtil {

    // Patterns for password validation
    static Pattern regexSpecialCharacter = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
    static Pattern regexSameCharacter = Pattern.compile("[a-zA-Z]{3,}");

    // Patterns for username validation
    static Pattern regexUsername = Pattern.compile("[a-zA-Z0-9_-]");

    public static boolean isValidID(@NotNull String idNumber) {
        return idNumber.length() == AppConstants.ValidationRules.FIELD_ID_LENGTH;
    }

    public static boolean isValidUsername(@NotNull String username) {
        return username.length() >= AppConstants.ValidationRules.FIELD_USERNAME_MIN_LENGTH;
    }

    @Contract(pure = true)
    public static boolean isValidCountryCode(@NotNull String countryCode) {
        return countryCode.equals(AppConstants.ValidationRules.COUNTRY_CODE_KSA);
    }

    public static boolean isValidMobile(@NotNull String mobile) {
        return mobile.length() == 9;
//        return Pattern.matches("^(?:5)[0-9]{8}$", mobile);
    }

    public static boolean isValidPassword(@NotNull String password) {
        return password.length() >= AppConstants.ValidationRules.FIELD_PASSWORD_LENGTH;
    }

    public static boolean isMatchingPasswords(@NotNull String password, @NotNull String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static boolean isValidOtp(@NotNull String otp) {
        return otp.length() == AppConstants.ValidationRules.FIELD_OTP_LENGTH || otp.length() == 6;
    }

    // Check if entered text length is valid , 8 - 20
    public static boolean isLengthEnough(String entryText){
        return entryText.length() > 8 && entryText.length() < 20;
    }

    // No Arabic Characters just latin
    public static boolean isEnglish(String entryText){
        for (char charac: entryText.toCharArray()) {
            if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC){
                return false;
            }
        }

        return true;
    }

    // Check if contains digit
    public static boolean isContainDigit(String password) {
        return password.matches(".*\\d.*");
    }

    // Check if contains special characters
    public static boolean isContainSpecialCharacter(String password){
        return regexSpecialCharacter.matcher(password).find();
    }

    public static boolean isRepeatingSameCharacters(String password){
        for(int i = 0; i < password.length(); i++){
            int repeatedCount = 0;
            for(int j = 0; j < password.length(); j++){
                if(password.charAt(i) == password.charAt(j)){
                    repeatedCount++;
                }
                if(repeatedCount >= 3)
                    return true;
            }
        }

        return false;
    }

    public static boolean isRepeatingSameBlock(String password){
        String substr = "";

        for(int i = 0; i < password.length(); i++){
            for (int j = i+2; j < password.length(); j++){
                substr = password.substring(i, j);

                String str = password;
                String findStr = substr;
                int lastIndex = 0;
                int count = 0;

                while(lastIndex != -1){

                    lastIndex = str.indexOf(findStr,lastIndex);

                    if(lastIndex != -1){
                        count ++;
                        lastIndex += findStr.length();
                    }
                }

                if(count >= 2)
                    return true;
            }
        }

        return false;
    }

    // Check if username contains valid characters
    public static boolean isContainValidCharactersUsername(String username){
        return regexUsername.matcher(username).find();
    }
}
