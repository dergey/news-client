package com.sergey.zhuravlev.rpodmp.lab2.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.zhuravlev.rpodmp.lab2.R;
import com.sergey.zhuravlev.rpodmp.lab2.database.BitmapCache;
import com.sergey.zhuravlev.rpodmp.lab2.model.Article;

public class ArticleActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView contentTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.contentImageView);
        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        restoreValue();
    }

    private void restoreValue() {
        Article article = getIntent().getParcelableExtra(Article.class.getCanonicalName());
        titleTextView.setText(article.getTitle());
        contentTextView.setText(article.getDescription());
        Bitmap image = BitmapCache.getInstance().getBitmapFromMemCache(article.getUrlToImage());
        imageView.setImageBitmap(image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
