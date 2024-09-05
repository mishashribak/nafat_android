package com.nafaz.android.api.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.core.APIBulldozer;
import com.nafaz.android.api.core.NafazCookieJar;
import com.nafaz.android.api.interfaces.APIs;
import com.nafaz.android.manager.PrefManager;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;


public class CaptchaService {
    private static final String TAG = CaptchaService.class.getSimpleName();

    public void start(final ApiCallback callback) {

        final APIs apIs = APIBulldozer.createAPI(APIs.class);

        apIs.getCaptcha()
                .enqueue(new Callback<ResponseBody>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        Timber.d("%s Response: %s", TAG, new Gson().toJson(response.body()));

                        if (null == response.body())
                            return;

//                        String cookie = response.headers().get("Set-Cookie");
//                        PrefManager.getInstance(context).setPreferences("cookie", cookie);

                        if (response.isSuccessful()) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onFail(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}
