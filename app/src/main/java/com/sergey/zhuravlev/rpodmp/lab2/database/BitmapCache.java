package com.sergey.zhuravlev.rpodmp.lab2.database;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapCache {

    private static BitmapCache INSTANCE;

    private final LruCache<String, Bitmap> memoryCache;


    public static BitmapCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BitmapCache();
        }
        return INSTANCE;
    }

    BitmapCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

}
