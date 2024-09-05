package com.nafaz.android.entity;

import android.app.Activity;
import android.view.View;

import com.nafaz.android.app.NafazConfig;
import com.nafaz.android.ui.activity.AuthenticationMethodsActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthLogic {

    public static Set<MethodType> methodTypes = new HashSet<>();
    public static int currentStep = 0;

    public static AuthenticationMethod authMethods;
    public static List<String> availableActions;

    @Contract(pure = true)
    public static boolean isMobileVerified() {
        if(methodTypes == null)
            return false;
        return methodTypes.contains(MethodType.MOBILE);
    }

    @Contract(pure = true)
    public static boolean isFaceVerified() {
        if(methodTypes == null)
            return false;
        return methodTypes.contains(MethodType.FACE);
    }

    @Contract(pure = true)
    public static boolean isUsernameVerified() {
        if(methodTypes == null)
            return false;
        return methodTypes.contains(MethodType.USERNAME);
    }

    @Contract(pure = true)
    public static boolean isTOTPVerified() {
        if(methodTypes == null)
            return false;
        return methodTypes.contains(MethodType.TOTP);
    }

    // In later stages
//    @Contract(pure = true)
//    public static boolean isIdNumVerified() {
//        return methodTypes.contains(MethodType.ID_NUM);
//    }
//
//    @Contract(pure = true)
//    public static boolean areSecurityQuestionsSet() {
//        return methodTypes.contains(MethodType.PROTECTION_QUES);
//    }
//
//    @Contract(pure = true)
//    public static boolean isEmailVerified() {
//        return methodTypes.contains(MethodType.EMAIL);
//    }

    public static boolean hasAllVerifiedMethodsInOneStep(){
        if (null != AuthLogic.authMethods) {
            final List<AllowedStepsOne> stepOneList = authMethods.arrayOfStepsOne;
            for (int i = 0; i < stepOneList.size(); i++) {
                String stepName = stepOneList.get(i).stepName;
                if (stepName.equals("OTP")){
                    if( !isMobileVerified())
                        return false;
                }

                if (stepName.equals("CHECK_FACE")){
                    if(!isFaceVerified())
                        return false;
                }

                if (stepName.equals("UP")){
                    if(!isUsernameVerified())
                        return false;
                }

            }
            return true;
        }else{
            return false;
        }
    }

    public static boolean hasOneVerifiedMethodsInOneStep(){
        if (null != AuthLogic.authMethods) {
            final List<AllowedStepsOne> stepOneList = authMethods.arrayOfStepsOne;
            for (int i = 0; i < stepOneList.size(); i++) {
                String stepName = stepOneList.get(i).stepName;
                if (stepName.equals("OTP")){
                    if( isMobileVerified())
                        return true;
                }

                if (stepName.equals("CHECK_FACE")){
                    if(isFaceVerified())
                        return true;
                }

                if (stepName.equals("UP")){
                    if(isUsernameVerified())
                        return true;
                }

                if (stepName.equals("CHECK_TOTP")){
                    if(isTOTPVerified())
                        return true;
                }

            }
            return false;
        }else{
            return false;
        }
    }

    public static boolean hasOneVerifiedMethodsInTwoStep(){
        if (null != AuthLogic.authMethods) {
            final List<AllowedStepsTwo> stepTwoList = authMethods.arrayOfStepsTwo;
            for (int i = 0; i < stepTwoList.size(); i++) {
                String stepName = stepTwoList.get(i).stepName;
                if (stepName.equals("OTP")){
                    if( isMobileVerified())
                        return true;
                }

                if (stepName.equals("CHECK_FACE")){
                    if(isFaceVerified())
                        return true;
                }

                if (stepName.equals("UP")){
                    if(isUsernameVerified())
                        return true;
                }

                if (stepName.equals("CHECK_TOTP")){
                    if(isTOTPVerified())
                        return true;
                }

            }
            return false;
        }else{
            return false;
        }
    }

    public static boolean hasOneStep() {
        return authMethods.isResponseHaveOneStep();
    }

    public static void add(MethodType methodType) {
        if (null == methodTypes)
            methodTypes = new HashSet<>();
        methodTypes.add(methodType);
    }

    @Nullable
    public static MethodType decideRouteFromFirstStep(Activity context) {

        if (authMethods.isResponseHaveEmptySteps())
            return null;

        if (!hasOneStep()) {
            AuthenticationMethodsActivity.open(context);
            return MethodType.ALL;
        }

        for (int i = 0; i < authMethods.arrayOfStepsOne.size(); i++) {
            String stepName = authMethods.arrayOfStepsOne.get(i).stepName;

            if (stepName.equals(MethodType.MOBILE.name())) {
                return MethodType.MOBILE;
            }

            if (stepName.equals(MethodType.FACE.name())) {
                return MethodType.FACE;
            }
        }

        return MethodType.ALL;
    }

    public static void removeCompletedAuthFromSecondArray(MethodType methodType) {
        for (int i = 0; i < authMethods.arrayOfStepsTwo.size(); i++) {
            String stepName = authMethods.arrayOfStepsTwo.get(i).stepName;
            if (stepName.equals(methodType.name())) {
                authMethods.arrayOfStepsTwo.remove(i);
            }
        }
    }

    public static MethodType getNextStepFromSecondArray() {
        if (authMethods.arrayOfStepsTwo.size() >= 2)
            return MethodType.ALL;

        for (int i = 0; i < authMethods.arrayOfStepsTwo.size(); i++) {
            String stepName = authMethods.arrayOfStepsTwo.get(i).stepName;

            if (stepName.equals(MethodType.MOBILE.name())) {
                return MethodType.MOBILE;
            }

            if (stepName.equals(MethodType.FACE.name())) {
                return MethodType.FACE;
            }

            // For later work
//            if (stepName.equals(MethodType.EMAIL.name())) {
//                return MethodType.EMAIL;
//            }
//
//            if (stepName.equals(MethodType.ID_NUM.name())) {
//                return MethodType.ID_NUM;
//            }
//
//            if (stepName.equals(MethodType.PROTECTION_QUES.name())) {
//                return MethodType.PROTECTION_QUES;
//            }
        }

        return MethodType.ALL;
    }

    public enum MethodType {
        MOBILE,
        USERNAME,
        FACE,
        ID_NUM,
        PROTECTION_QUES,
        EMAIL,
        ALL,
        TOTP
    }

}
