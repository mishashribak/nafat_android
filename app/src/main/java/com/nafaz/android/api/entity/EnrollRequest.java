package com.nafaz.android.api.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class EnrollRequest {
    private String deviceName;
    private String description;
}
