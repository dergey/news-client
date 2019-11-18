package com.sergey.zhuravlev.rpodmp.lab2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.sergey.zhuravlev.rpodmp.lab2.R;
import com.sergey.zhuravlev.rpodmp.lab2.adapter.SourceAdapter;
import com.sergey.zhuravlev.rpodmp.lab2.database.DatabaseHolder;

public class SourceActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_source;

    private SourceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new SourceAdapter(this, DatabaseHolder.getInstance(this));
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirmation, menu);
        return true;
    }

}
