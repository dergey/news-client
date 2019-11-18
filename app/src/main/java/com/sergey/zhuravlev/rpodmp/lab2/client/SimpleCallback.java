package com.sergey.zhuravlev.rpodmp.lab2.client;

public interface SimpleCallback<T> {

    void onResponse(T response);

    void onFailure(Throwable t);

}
