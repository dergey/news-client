package com.sergey.zhuravlev.rpodmp.lab2.exception;

import com.sergey.zhuravlev.rpodmp.lab2.dto.ErrorDto;

import lombok.Getter;

@Getter
public class ErrorResponseException extends Exception {

    private ErrorDto errorDto;
    private final int status;

    public ErrorResponseException(int status) {
        super("Status " + status);
        this.status = status;
    }

    public ErrorResponseException(int status, ErrorDto errorDto) {
        super();
        this.status = status;
        this.errorDto = errorDto;
    }

}
