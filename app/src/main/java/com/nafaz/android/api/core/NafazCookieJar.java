package com.nafaz.android.api.core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class NafazCookieJar implements CookieJar {

    private List<Cookie> cookies = new ArrayList<>();

    @Override
    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookieList) {
        // Only add Cookies from /Identity API
        if (url.encodedPath().endsWith("identity")) {
            cookies.clear();
            cookies.addAll(cookieList);
        }
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
        // Load Cookies for all APIs except the following (as they don't need Cookie):
        if (!url.encodedPath().endsWith("authentication") &&
                !url.encodedPath().endsWith("authentication/disable") &&
                !url.encodedPath().endsWith("profile") &&
                !url.encodedPath().endsWith("identity")) {

            return cookies;
        }

        return new ArrayList<>();
    }
}
