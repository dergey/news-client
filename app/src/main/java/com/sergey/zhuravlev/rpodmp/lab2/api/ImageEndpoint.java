package com.sergey.zhuravlev.rpodmp.lab2.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageEndpoint {

    @GET("{name}")
    Call<ResponseBody> download(@Path("path") String path);

}
