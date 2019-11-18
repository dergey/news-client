package com.sergey.zhuravlev.rpodmp.lab2.api;

import com.sergey.zhuravlev.rpodmp.lab2.dto.SourcesPageDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SourcesEndpoint {

    @GET("v1/sources")
    Call<SourcesPageDto> getAllSources(@Query(value = "language") String language);

}
