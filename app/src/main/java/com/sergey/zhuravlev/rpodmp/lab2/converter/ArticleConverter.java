package com.sergey.zhuravlev.rpodmp.lab2.converter;

import com.sergey.zhuravlev.rpodmp.lab2.dto.ArticleDto;
import com.sergey.zhuravlev.rpodmp.lab2.dto.SourceDto;
import com.sergey.zhuravlev.rpodmp.lab2.model.Article;
import com.sergey.zhuravlev.rpodmp.lab2.model.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArticleConverter {

    public static List<Article> getArticles(Collection<ArticleDto> articleDtos) {
        List<Article> result = new ArrayList<>();
        for (ArticleDto articleDto : articleDtos) {
            result.add(getArticle(articleDto));
        }
        return result;
    }

    public static Article getArticle(ArticleDto articleDto) {
        return new Article(null,
                articleDto.getAuthor(),
                articleDto.getDescription(),
                articleDto.getTitle(),
                articleDto.getUrl(),
                articleDto.getUrlToImage(),
                articleDto.getPublishedAt()
        );
    }

}
