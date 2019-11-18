package com.sergey.zhuravlev.rpodmp.lab2.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergey.zhuravlev.rpodmp.lab2.api.ArticleEndpoint;
import com.sergey.zhuravlev.rpodmp.lab2.api.ImageEndpoint;
import com.sergey.zhuravlev.rpodmp.lab2.api.SourcesEndpoint;
import com.sergey.zhuravlev.rpodmp.lab2.dto.ArticlesPageDto;
import com.sergey.zhuravlev.rpodmp.lab2.dto.ErrorDto;
import com.sergey.zhuravlev.rpodmp.lab2.dto.SourcesPageDto;
import com.sergey.zhuravlev.rpodmp.lab2.exception.ErrorResponseException;
import com.sergey.zhuravlev.rpodmp.lab2.model.Source;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Client {

    private final static String NEWS_API_KEY = "b9d725b6ea4e490b9573223baa125028";

    @Getter
    private static final Client instance = new Client();

    public static final String SERVER_URL = "https://newsapi.org";

    @Setter
    @Getter
    private Context context;

    private OkHttpClient httpClient;

    private ObjectMapper objectMapper;

    private ArticleEndpoint articleEndpoint;
    private SourcesEndpoint sourcesEndpoint;

    @SuppressLint("CheckResult")
    public void init(Context context) {
        instance.setContext(context);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false);
        httpClient = builder.build();

        objectMapper = new ObjectMapper();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        articleEndpoint = retrofit.create(ArticleEndpoint.class);
        sourcesEndpoint = retrofit.create(SourcesEndpoint.class);
    }

    public void articlesPage(SimpleCallback<ArticlesPageDto> callback, String source, String sortBy) {
        articleEndpoint
                .getAllArticles(NEWS_API_KEY, source, sortBy)
                .enqueue(new ErrorHandlerSimpleCallback<>(callback));
    }

    public void sourcesPage(SimpleCallback<SourcesPageDto> callback, String language) {
        sourcesEndpoint
                .getAllSources(language)
                .enqueue(new ErrorHandlerSimpleCallback<>(callback));
    }

    public void imageDownload(String path, SimpleCallback<ResponseBody> callback) {
        if (path != null && !path.isEmpty()) {
            final Request request = new Request.Builder().url(path).build();
            httpClient
                    .newCall(request)
                    .enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException t) {
                            callback.onFailure(t);
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            callback.onResponse(response.body());
                        }
                    });
        }
    }

    public void subscribe(Source source) {

    }

    @AllArgsConstructor
    class ErrorHandlerCallback<T> implements Callback<T> {

        private Callback<T> innerCallback;

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (successfulCode(response.code()) || redirectionCode(response.code())) {
                innerCallback.onResponse(call, response);
            } else {
                try {
                    if (response.errorBody() != null) {
                        String body = response.errorBody().string();
                        ErrorDto errorDto = objectMapper.readValue(body, ErrorDto.class);
                        innerCallback.onFailure(call, new ErrorResponseException(response.code(), errorDto));
                        return;
                    }
                    Log.d("Auction.Client", "Parse exception. Nullable body!");
                } catch (IOException e) {
                    Log.d("Auction.Client", "Parse exception!\n" + Log.getStackTraceString(e));
                }
                innerCallback.onFailure(call, new ErrorResponseException(response.code()));
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            innerCallback.onFailure(call, t);
        }

        private boolean successfulCode(int code) {
            return code >= 200 && code < 300;
        }

        private boolean redirectionCode(int code) {
            return code >= 300 && code < 400;
        }
    }

    class ErrorHandlerSimpleCallback<T> implements Callback<T> {

        private SimpleCallback<T> innerCallback;

        public ErrorHandlerSimpleCallback(SimpleCallback<T> innerCallback) {
            this.innerCallback = innerCallback;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (successfulCode(response.code()) || redirectionCode(response.code())) {
                innerCallback.onResponse(response.body());
            } else {
                try {
                    if (response.errorBody() != null) {
                        String body = response.errorBody().string();
                        ErrorDto errorDto = objectMapper.readValue(body, ErrorDto.class);
                        innerCallback.onFailure(new ErrorResponseException(response.code(), errorDto));
                        return;
                    }
                } catch (IOException ignored) {
                }
                innerCallback.onFailure(new ErrorResponseException(response.code()));
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            innerCallback.onFailure(t);
        }

        private boolean successfulCode(int code) {
            return code >= 200 && code < 300;
        }

        private boolean redirectionCode(int code) {
            return code >= 300 && code < 400;
        }
    }

}

