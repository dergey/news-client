package com.sergey.zhuravlev.rpodmp.lab2.dto;

import com.sergey.zhuravlev.rpodmp.lab2.enums.ResponseError;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourcesPageDto {

    private ResponseError status;
    private List<SourceDto> sources;

}
