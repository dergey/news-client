package com.sergey.zhuravlev.rpodmp.lab2.dto;

import com.sergey.zhuravlev.rpodmp.lab2.enums.ResponseError;
import com.sergey.zhuravlev.rpodmp.lab2.model.Article;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesPageDto {

    private ResponseError status;
    private String source;
    private String sortBy;
    private List<ArticleDto> articles;

}
