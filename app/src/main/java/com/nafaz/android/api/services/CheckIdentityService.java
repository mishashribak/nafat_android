package com.nafaz.android.api.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.core.APIBulldozer;
import com.nafaz.android.api.entity.CheckIdRequest;
import com.nafaz.android.api.entity.CheckIdResponse;
import com.nafaz.android.api.helper.ErrorParser;
import com.nafaz.android.api.interfaces.APIs;
import com.nafaz.android.manager.PrefManager;

import org.jetbrains.annotations.NotNull;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.nafaz.android.api.constants.APIConstants.ResponseCode.CODE_200;
import static com.nafaz.android.manager.PrefManager.Keys.KEY_LANGUAGE;

public class CheckIdentityService {

    private static final String TAG = CheckIdentityService.class.getSimpleName();

    public void start(Context context, CheckIdRequest request, final ApiCallback callback) {

        final APIs apIs = APIBulldozer.createAPI(APIs.class);

        apIs.checkIdentity(request)
                .enqueue(new Callback<CheckIdResponse>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(@NotNull Call<CheckIdResponse> call, @NotNull Response<CheckIdResponse> response) {
                        Timber.d("%s Response: %s", TAG, new Gson().toJson(response.body()));

                        if (null == response.body())
                            return;

                        // get header value
//                        String cookie = response.headers().get("Set-Cookie");
//                        PrefManager.getInstance(context).setPreferences("cookie", cookie);

//                        Log.e("cookie", cookie);

                        if (response.isSuccessful()) {

                            if (response.body().code == CODE_200)
                                callback.onSuccess(response.body());
                            else
                                callback.onFail(ErrorParser.getMessage(response.body()));

                        } else {
                            Timber.e(response.errorBody().toString());
                            callback.onFail(ErrorParser.getMessage(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<CheckIdResponse> call, @NotNull Throwable t) {
                        Timber.tag(TAG).e(t);
                        callback.onError(t.getMessage());
                    }
                });
    }
}
