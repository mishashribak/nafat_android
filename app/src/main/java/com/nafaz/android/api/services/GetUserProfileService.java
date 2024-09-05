package com.nafaz.android.api.services;

import com.google.gson.Gson;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.core.APIBulldozer;
import com.nafaz.android.api.entity.ApiResponse;
import com.nafaz.android.api.helper.ErrorParser;
import com.nafaz.android.api.interfaces.APIs;
import com.nafaz.android.entity.UserProfile;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.nafaz.android.api.constants.APIConstants.ResponseCode.CODE_200;

public class GetUserProfileService {

    private static final String TAG = GetUserProfileService.class.getSimpleName();

    public void start(BigInteger personId, final ApiCallback callback) {

        final APIs apIs = APIBulldozer.createAPI(APIs.class);

        apIs.getUserProfile(personId)
                .enqueue(new Callback<ApiResponse<UserProfile>>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(@NotNull Call<ApiResponse<UserProfile>> call, @NotNull Response<ApiResponse<UserProfile>> response) {
                        Timber.d("%s Response: %s", TAG, new Gson().toJson(response.body()));

                        if (null == response.body())
                            return;

                        if (response.isSuccessful()) {

                            if (response.body().code == CODE_200)
                                callback.onSuccess(response.body().data);
                            else
                                callback.onFail(ErrorParser.getMessage(response.body()));

                        } else {
                            Timber.e(response.errorBody().toString());
                            callback.onFail(ErrorParser.getMessage(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ApiResponse<UserProfile>> call, @NotNull Throwable t) {
                        Timber.tag(TAG).e(t);
                        callback.onError(t.getMessage());
                    }
                });
    }
}
