package com.sergey.zhuravlev.rpodmp.lab2.api;

import com.sergey.zhuravlev.rpodmp.lab2.dto.ArticlesPageDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ArticleEndpoint {

    @GET("v1/articles")
    Call<ArticlesPageDto> getAllArticles(@Header("X-Api-Key") String apiKey,
                                         @Query(value = "source") String source,
                                         @Query(value = "sortBy") String sortBy);


}
