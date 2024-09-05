package com.nafaz.android.api.core;

import com.google.gson.GsonBuilder;
import com.nafaz.android.R;
import com.nafaz.android.app.App;
import com.nafaz.android.manager.PrefManager;
import com.nafaz.android.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIBulldozer {

    static  HashSet<String> cookies = new HashSet<>();
    static class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
//                Methods.setCookies(MyApplication.getAppContext(), cookies);
            }
            return originalResponse;
        }
    }

    static class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
//            HashSet<String> preferences = Methods.getCookies(MyApplication.getAppContext());
            for (String cookie : cookies) {
                builder.addHeader("Cookie", cookie);
            }
            return chain.proceed(builder.build());
        }
    }

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(
                    GsonConverterFactory.create(new GsonBuilder().setLenient().create())
            );

    private static NafazCookieJar cookieJar = new NafazCookieJar();

    @NotNull
    public static <S> S createAPI(Class<S> apiClass) {

        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
                .cookieJar(cookieJar)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.interceptors().add(new AddCookiesInterceptor());
        okHttpClient.interceptors().add(new ReceivedCookiesInterceptor());

        Retrofit retrofit = retrofitBuilder.client(
                okHttpClient.build())
                .baseUrl(Utils.getString(R.string.BASE_URL))
                .build();
        return retrofit.create(apiClass);
    }
}
