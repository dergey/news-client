package com.sergey.zhuravlev.rpodmp.lab2.dto;

import com.sergey.zhuravlev.rpodmp.lab2.model.Country;
import com.sergey.zhuravlev.rpodmp.lab2.model.Language;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourceDto {

    private String id;
    private String name;
    private String description;
    private String url;
    private String category;

    private Language language;
    private Country country;

    private ImageDto urlsToLogos;

    private List<String> sortBysAvailable;

}
