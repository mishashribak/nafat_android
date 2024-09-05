package com.nafaz.android.api.interfaces;


import com.nafaz.android.api.entity.ApiResponse;
import com.nafaz.android.api.entity.CheckFaceRequest;
import com.nafaz.android.api.entity.CheckIdRequest;
import com.nafaz.android.api.entity.CheckIdResponse;
import com.nafaz.android.api.entity.CheckMobileRequest;
import com.nafaz.android.api.entity.CheckOtpRequest;
import com.nafaz.android.api.entity.CheckTOTPRequest;
import com.nafaz.android.api.entity.ConfirmMobileRequest;
import com.nafaz.android.api.entity.DisableAuthMethodRequest;
import com.nafaz.android.api.entity.EnrollRequest;
import com.nafaz.android.api.entity.EnrollResponse;
import com.nafaz.android.api.entity.LoginRequest;
import com.nafaz.android.api.entity.OPTAuthRequest;
import com.nafaz.android.api.entity.RegisterMobileRequest;
import com.nafaz.android.api.entity.RegisterNewUserRequest;
import com.nafaz.android.entity.AuthenticationMethod;
import com.nafaz.android.entity.UserProfile;

import java.math.BigInteger;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIs {

    @GET("authentication")
    Call<ApiResponse<List<AuthenticationMethod>>> getAuthenticationMethods(@Query("personId") BigInteger personId);

    @POST("authentication/disable")
    Call<ApiResponse<List<AuthenticationMethod>>> disableAuthenticationMethod(@Body DisableAuthMethodRequest request);

    /**
     * Check user API
     *
     * @param request {@link CheckIdRequest}
     * @return {@link List<AuthenticationMethod>>}
     */
    @POST("identity")
    Call<CheckIdResponse> checkIdentity(@Body CheckIdRequest request);

    @GET("enrollTOTP")
    Call<EnrollResponse> enrollTOTP();

    @GET("reEnrollTOTP")
    Call<EnrollResponse> reEnrollTOTP();

    @GET("profile")
    Call<ApiResponse<UserProfile>> getUserProfile(@Query("personId") BigInteger personId);

    @POST("registerNewUser")
    Call<ApiResponse> registerNewUser(@Body RegisterNewUserRequest request);

    /**
     * Check face API
     *
     * @param request {@link CheckFaceRequest}
     * @return {@link ApiResponse}
     */
    @POST("face")
    Call<ApiResponse> checkFace(@Body CheckFaceRequest request);

    /**
     * Check mobile API
     *
     * @param request {@link CheckMobileRequest}
     * @return {@link ApiResponse}
     */
    @POST("mobile")
    Call<ApiResponse> checkMobile(@Body CheckMobileRequest request);

    @POST("checkTOTP")
    Call<ApiResponse> checkTOTP(@Body CheckTOTPRequest request);

    @POST("otpAuthentication")
    Call<ApiResponse> otpAuthentication(@Body OPTAuthRequest request);

    /**
     * Register mobile API
     *
     * @param request {@link RegisterMobileRequest}
     * @return {@link ApiResponse}
     */
    @POST("registerMobile")
    Call<ApiResponse> registerMobile(@Body RegisterMobileRequest request);


    @POST("upAuthentication")
    Call<ApiResponse> loginUsername(@Header("Cookie") String sessionIdAndToken, @Body LoginRequest request);


    @GET("getCaptcha")
//    Call<ResponseBody> getCaptcha();
//    @GET("simplecaptcha.jpeg")
    Call<ResponseBody> getCaptcha();

    /**
     * Check otp for current mobile
     *
     * @param request {@link CheckOtpRequest}
     * @return {@link ApiResponse}
     */
    @POST("mobile/otp")
    Call<ApiResponse> CheckOtp(@Body CheckOtpRequest request);

    /**
     * Check Otp for new mobile API
     *
     * @param request {@link ConfirmMobileRequest}
     * @return {@link ApiResponse}
     */
    @POST("confirmMobile")
    Call<ApiResponse> confirmMobile(@Body ConfirmMobileRequest request);

}
