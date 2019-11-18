package com.sergey.zhuravlev.rpodmp.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private String status;
    private String code;
    private String message;

}
