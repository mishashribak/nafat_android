package com.nafaz.android.event;

import com.nafaz.android.entity.AuthLogic;

import org.jetbrains.annotations.Contract;

public class AuthMethodDoneEvent {

    public AuthLogic.MethodType methodType;

    @Contract(pure = true)
    public AuthMethodDoneEvent(AuthLogic.MethodType methodType) {
        this.methodType = methodType;
    }
}
