package com.sergey.zhuravlev.rpodmp.lab2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sergey.zhuravlev.rpodmp.lab2.database.NewsContract.*;
import com.sergey.zhuravlev.rpodmp.lab2.model.Article;
import com.sergey.zhuravlev.rpodmp.lab2.model.Country;
import com.sergey.zhuravlev.rpodmp.lab2.model.Language;
import com.sergey.zhuravlev.rpodmp.lab2.model.Source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    @FunctionalInterface
    public interface DatabaseListener extends EventListener {
        void databaseChanged();
    }

    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 2;

    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private List<DatabaseListener> listeners;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.listeners = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + ArticleEntry.TABLE_NAME + " ("
                + NewsContract.ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ArticleEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + ArticleEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + ArticleEntry.COLUMN_AUTHOR + " TEXT, "
                + ArticleEntry.COLUMN_URL + " TEXT, "
                + ArticleEntry.COLUMN_URL_TO_IMAGE + " TEXT, "
                + ArticleEntry.COLUMN_PUBLISHED_DATE + " DATE); ";

        db.execSQL(SQL_CREATE_USER_TABLE);

        String SQL_CREATE_SOURCE_TABLE = "CREATE TABLE " + SourceEntry.TABLE_NAME + " ("
                + NewsContract.SourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SourceEntry.COLUMN_API_ID + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_URL + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_LANGUAGE + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_COUNTRY + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_SMALL + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_MEDIUM + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_LARGE + " TEXT NOT NULL, "
                + SourceEntry.COLUMN_SORT_BY_AVAILABLE + " TEXT);";

        db.execSQL(SQL_CREATE_SOURCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (article.getId() != null)
            values.put(ArticleEntry._ID, article.getId());
        values.put(ArticleEntry.COLUMN_TITLE, article.getTitle());
        values.put(ArticleEntry.COLUMN_DESCRIPTION, article.getDescription());
        values.put(ArticleEntry.COLUMN_AUTHOR, article.getAuthor());
        values.put(ArticleEntry.COLUMN_URL, article.getUrl());
        values.put(ArticleEntry.COLUMN_URL_TO_IMAGE, article.getUrlToImage());
        values.put(ArticleEntry.COLUMN_PUBLISHED_DATE, dateFormat.format(article.getPublishedAt()));
        db.insert(ArticleEntry.TABLE_NAME, null, values);
        fireDataChanged();
    }

    public void addSource(Source source) {
        addSource(source, true);
    }

    public void addAllSources(Collection<Source> sources) {
        for (Source source : sources) {
            addSource(source, false);
        }
    }

    private void addSource(Source source, boolean fireChanges) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (source.getId() != null)
            values.put(SourceEntry._ID, source.getId());
        values.put(SourceEntry.COLUMN_API_ID, source.getApiId());
        values.put(SourceEntry.COLUMN_NAME, source.getName());
        values.put(SourceEntry.COLUMN_DESCRIPTION, source.getDescription());
        values.put(SourceEntry.COLUMN_URL, source.getUrl());
        values.put(SourceEntry.COLUMN_CATEGORY, source.getCategory());
        values.put(SourceEntry.COLUMN_LANGUAGE, source.getLanguage().getCode());
        values.put(SourceEntry.COLUMN_COUNTRY, source.getCountry().getCode());
        values.put(SourceEntry.COLUMN_SMALL, source.getSmall());
        values.put(SourceEntry.COLUMN_MEDIUM, source.getMedium());
        values.put(SourceEntry.COLUMN_LARGE, source.getLarge());
        values.put(SourceEntry.COLUMN_SORT_BY_AVAILABLE, collectionToString(source.getSortBysAvailable()));
        db.insert(SourceEntry.TABLE_NAME, null, values);
        if (fireChanges) {
            fireDataChanged();
        }
    }

    public void addAllArticle(Collection<Article> articles) {
        for (Article article : articles) {
            addArticle(article);
        }
        fireDataChanged();
    }

    public void addListener(DatabaseListener listener) {
        listeners.add(listener);
    }

    private void fireDataChanged() {
        for (DatabaseListener listener : listeners) {
            listener.databaseChanged();
        }
    }

    public List<Article> getAllArticle() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Article> articles = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsContract.ArticleEntry.TABLE_NAME, null, null);

        int idColumnIndex = cursor.getColumnIndex(ArticleEntry._ID);
        int titleColumnIndex = cursor.getColumnIndex(ArticleEntry.COLUMN_TITLE);
        int descriptionColumnIndex = cursor.getColumnIndex(ArticleEntry.COLUMN_DESCRIPTION);
        int authorColumnIndex = cursor.getColumnIndex(ArticleEntry.COLUMN_AUTHOR);
        int urlColumnIndex = cursor.getColumnIndex(ArticleEntry.COLUMN_URL);
        int urlToImageColumnIndex = cursor.getColumnIndex(ArticleEntry.COLUMN_URL_TO_IMAGE);
        int publishedDateColumnIndex = cursor.getColumnIndex(ArticleEntry.COLUMN_PUBLISHED_DATE);

        while (cursor.moveToNext()) {
            int currentID = cursor.getInt(idColumnIndex);
            String currentTitle = cursor.getString(titleColumnIndex);
            String currentDescription = cursor.getString(descriptionColumnIndex);
            String currentAuthor = cursor.getString(authorColumnIndex);
            String currentUrl = cursor.getString(urlColumnIndex);
            String currentUrlToImage = cursor.getString(urlToImageColumnIndex);
            String currentPublishedDate = cursor.getString(publishedDateColumnIndex);

            Article current = new Article(
                    currentID,
                    currentAuthor,
                    currentDescription,
                    currentTitle,
                    currentUrl,
                    currentUrlToImage,
                    parseDate(currentPublishedDate));

            current.setId(currentID);
            articles.add(current);
        }

        cursor.close();
        return articles;
    }

    public List<Source> getAllSources() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Source> sources = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + NewsContract.SourceEntry.TABLE_NAME, null, null);

        int idColumnIndex = cursor.getColumnIndex(SourceEntry._ID);
        int apiIdColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_API_ID);
        int nameColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_NAME);
        int descriptionColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_DESCRIPTION);
        int urlColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_URL);
        int categoryColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_CATEGORY);
        int languageColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_LANGUAGE);
        int countryColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_COUNTRY);
        int smallColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_SMALL);
        int mediumColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_MEDIUM);
        int largeColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_LARGE);
        int sortBysAvailableColumnIndex = cursor.getColumnIndex(SourceEntry.COLUMN_SORT_BY_AVAILABLE);

        while (cursor.moveToNext()) {
            int currentID = cursor.getInt(idColumnIndex);
            String currentApiId = cursor.getString(apiIdColumnIndex);
            String currentName = cursor.getString(nameColumnIndex);
            String currentDescription = cursor.getString(descriptionColumnIndex);
            String currentUrl = cursor.getString(urlColumnIndex);
            String currentCategory = cursor.getString(categoryColumnIndex);
            String currentLanguage = cursor.getString(languageColumnIndex);
            String currentCountry = cursor.getString(countryColumnIndex);
            String currentSmall = cursor.getString(smallColumnIndex);
            String currentMedium = cursor.getString(mediumColumnIndex);
            String currentLarge = cursor.getString(largeColumnIndex);
            String currentSortBysAvailable = cursor.getString(sortBysAvailableColumnIndex);

            Source source = new Source(
                    currentID,
                    currentApiId,
                    currentName,
                    currentDescription,
                    currentUrl,
                    currentCategory,
                    new Language(currentLanguage),
                    new Country(currentCountry),
                    currentSmall,
                    currentMedium,
                    currentLarge,
                    parseStringArray(currentSortBysAvailable));

            source.setId(currentID);
            sources.add(source);
        }

        cursor.close();
        return sources;
    }

    private static String collectionToString(List<String> value) {
        StringBuilder result = new StringBuilder();
        for (String s : value) {
            result.append(s);
        }
        return result.toString();
    }

    private static List<String> parseStringArray(String value) {
        List<String> result = null;
        try {
            result = Arrays.asList(value.split(","));
        } catch (Exception ignored) {
        }
        return result;
    }

    private static Date parseDate(String value) {
        Date result = null;
        try {
            result = dateFormat.parse(value);
        } catch (Exception ignored) {
        }
        return result;
    }

}
