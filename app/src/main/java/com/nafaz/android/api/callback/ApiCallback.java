package com.nafaz.android.api.callback;

public interface ApiCallback<T> {

    void onSuccess(T object);

    void onError(String errorMessage);

    void onFail(String failMessage);
}
