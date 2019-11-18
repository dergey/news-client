package com.sergey.zhuravlev.rpodmp.lab2.database;

import android.provider.BaseColumns;

public final class NewsContract {

    public static final class ArticleEntry implements BaseColumns {
        public final static String TABLE_NAME = "articles";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_AUTHOR = "author";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_TITLE= "title";
        public final static String COLUMN_URL = "url";
        public final static String COLUMN_URL_TO_IMAGE = "url_to_image";
        public final static String COLUMN_PUBLISHED_DATE = "published_at";
    }

    public static final class SourceEntry implements BaseColumns {
        public final static String TABLE_NAME = "sources";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_API_ID = "api_id";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_URL = "url";
        public final static String COLUMN_CATEGORY = "category";

        public final static String COLUMN_LANGUAGE = "language";
        public final static String COLUMN_COUNTRY = "country";

        public final static String COLUMN_SMALL = "small";
        public final static String COLUMN_MEDIUM = "medium";
        public final static String COLUMN_LARGE = "large";

        public final static String COLUMN_SORT_BY_AVAILABLE = "sortBysAvailable";
    }

}

