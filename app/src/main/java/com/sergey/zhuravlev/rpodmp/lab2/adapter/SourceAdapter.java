package com.sergey.zhuravlev.rpodmp.lab2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.zhuravlev.rpodmp.lab2.R;
import com.sergey.zhuravlev.rpodmp.lab2.client.Client;
import com.sergey.zhuravlev.rpodmp.lab2.client.SimpleCallback;
import com.sergey.zhuravlev.rpodmp.lab2.database.DatabaseHelper;
import com.sergey.zhuravlev.rpodmp.lab2.model.Source;

import java.util.List;

import okhttp3.ResponseBody;

public class SourceAdapter extends BaseAdapter implements DatabaseHelper.DatabaseListener {

    private DatabaseHelper helper;

    private Activity activity;

    private List<Source> sources;

    public SourceAdapter(Activity activity, DatabaseHelper helper) {
        this.sources = helper.getAllSources();
        this.helper = helper;
        this.activity = activity;
        helper.addListener(this);
    }

    @Override
    public int getCount() {
        return sources.size();
    }

    @Override
    public Object getItem(int position) {
        return sources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_item, parent, false);
        return createView(view, sources.get(position));
    }

    private View createView(View view, final Source source) {
        final TextView textView = view.findViewById(R.id.textView);
        final ImageView contentImageView = view.findViewById(R.id.imageView);

        view.setOnClickListener((e) -> {
            Intent intent = new Intent();
            intent.putExtra(Source.class.getCanonicalName(), source);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        });


        textView.setText(source.getName());

        //TODO Get DPI and images
        if (source.getMedium() != null && !source.getMedium().isEmpty()) {
            Client.getInstance().imageDownload(source.getMedium(), new SimpleCallback<ResponseBody>() {
                @Override
                public void onResponse(ResponseBody response) {
                    Bitmap bmp = BitmapFactory.decodeStream(response.byteStream());
                    contentImageView.setImageBitmap(bmp);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

        return view;
    }


    @Override
    public void databaseChanged() {
        this.sources = helper.getAllSources();
        this.notifyDataSetChanged();
    }

}
