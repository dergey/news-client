package com.sergey.zhuravlev.rpodmp.lab2.database;

import android.content.Context;

public class DatabaseHolder {

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) instance = new DatabaseHelper(context);
        return instance;
    }

}
