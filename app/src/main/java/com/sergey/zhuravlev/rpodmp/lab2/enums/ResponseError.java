package com.sergey.zhuravlev.rpodmp.lab2.enums;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseError {

    OK("ok"),
    ERROR("error");

    @JsonValue
    private final String code;

}
