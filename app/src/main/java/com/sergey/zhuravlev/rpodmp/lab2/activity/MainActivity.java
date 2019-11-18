package com.sergey.zhuravlev.rpodmp.lab2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.sergey.zhuravlev.rpodmp.lab2.R;
import com.sergey.zhuravlev.rpodmp.lab2.adapter.ArticleAdapter;
import com.sergey.zhuravlev.rpodmp.lab2.client.Client;
import com.sergey.zhuravlev.rpodmp.lab2.client.SimpleCallback;
import com.sergey.zhuravlev.rpodmp.lab2.converter.ArticleConverter;
import com.sergey.zhuravlev.rpodmp.lab2.converter.SourceConverter;
import com.sergey.zhuravlev.rpodmp.lab2.database.DatabaseHelper;
import com.sergey.zhuravlev.rpodmp.lab2.database.DatabaseHolder;
import com.sergey.zhuravlev.rpodmp.lab2.dto.ArticlesPageDto;
import com.sergey.zhuravlev.rpodmp.lab2.dto.SourcesPageDto;
import com.sergey.zhuravlev.rpodmp.lab2.exception.ErrorResponseException;
import com.sergey.zhuravlev.rpodmp.lab2.model.Article;
import com.sergey.zhuravlev.rpodmp.lab2.model.Source;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private final String APP_PREFERENCE = "News-Application";
    private final String APP_PREFERENCE_SUBSCRIPTION = "subscription";

    private String userSubscription;

    private DatabaseHelper databaseHelper;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = DatabaseHolder.getInstance(this);
        Client.getInstance().init(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAdditionSources());

        recyclerView = findViewById(R.id.recycler_view);
        initRecyclerView();

        updateSourcesIfNotExist("en");//todo add languages android get
        restoreSubscription();
        getArticle(userSubscription);
    }

    private void getArticle(String sourceName) {
        Client.getInstance().articlesPage(new SimpleCallback<ArticlesPageDto>() {
            @Override
            public void onResponse(ArticlesPageDto response) {
                List<Article> oldArticles = databaseHelper.getAllArticle();
                List<Article> articles = ArticleConverter.getArticles(response.getArticles());

                databaseHelper.addAllArticle(mergeArticles(oldArticles, articles));
            }

            @Override
            public void onFailure(Throwable t) {
                List<Article> articles = databaseHelper.getAllArticle();
                databaseHelper.addAllArticle(articles);
                if (t instanceof ErrorResponseException) {
                    Toast.makeText(MainActivity.this, ((ErrorResponseException) t).getErrorDto().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, sourceName, null);
    }

    private static Collection<Article> mergeArticles(List<Article> oldArticles, List<Article> articles) {
        return new ArrayList<>(
                Stream.of(oldArticles, articles)
                        .flatMap(List::stream)
                        .collect(Collectors.toMap(Article::getTitle,
                                d -> d,
                                (Article x, Article y) -> x))
                        .values());
    }

    private void restoreSubscription() {
        SharedPreferences sp = getSharedPreferences(APP_PREFERENCE,0);
        userSubscription = sp.getString(APP_PREFERENCE_SUBSCRIPTION, "");
    }

    private void updateSourcesIfNotExist(String language) {
        Client.getInstance().sourcesPage(new SimpleCallback<SourcesPageDto>() {
            @Override
            public void onResponse(SourcesPageDto response) {
                List<Source> sources = databaseHelper.getAllSources();
                if (sources.size() == 0) {
                    databaseHelper.addAllSources(SourceConverter.getSources(response.getSources()));
                }
                Toast.makeText(MainActivity.this, "All sources (" + sources.size()
                        + ") restore", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                List<Source> sources = databaseHelper.getAllSources();
                databaseHelper.addAllSources(sources);
            }
        }, language);
    }

    void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        ArticleAdapter adapter = new ArticleAdapter(this, databaseHelper);
        recyclerView.setAdapter(adapter);
    }

    private void showAdditionSources() {
        Intent intent = new Intent(MainActivity.this, SourceActivity.class);
        intent.setAction(RequestCode.ACTION_CREATE_USER);
        MainActivity.this.startActivityForResult(intent, RequestCode.REQUEST_SUBSCRIPTION);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.REQUEST_SUBSCRIPTION) {
                Source source = data.getParcelableExtra(Source.class.getCanonicalName());
                Client.getInstance().subscribe(source);
                getArticle(source.getApiId());
            }
        }
    }

}
