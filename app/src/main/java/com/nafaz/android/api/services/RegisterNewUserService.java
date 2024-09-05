package com.nafaz.android.api.services;


import com.google.gson.Gson;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.core.APIBulldozer;
import com.nafaz.android.api.entity.ApiResponse;
import com.nafaz.android.api.entity.RegisterNewUserRequest;
import com.nafaz.android.api.helper.ErrorParser;
import com.nafaz.android.api.interfaces.APIs;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.nafaz.android.api.constants.APIConstants.ResponseCode.CODE_200;

public class RegisterNewUserService {

    private static final String TAG = RegisterNewUserService.class.getSimpleName();

    public void start(RegisterNewUserRequest request, final ApiCallback callback) {

        final APIs apIs = APIBulldozer.createAPI(APIs.class);

        apIs.registerNewUser(request)
                .enqueue(new Callback<ApiResponse>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                        Timber.d("%s Response: %s", TAG, new Gson().toJson(response.body()));

                        if (null == response.body())
                            return;

                        if (response.isSuccessful()) {

                            if (response.body().code == CODE_200)
                                callback.onSuccess(null);
                            else
                                callback.onFail(ErrorParser.getMessage(response.body()));

                        } else {
                            Timber.e(response.errorBody().toString());
                            callback.onFail(ErrorParser.getMessage(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                        Timber.tag(TAG).e(t);
                        callback.onError(t.getMessage());
                    }
                });
    }
}
