package com.sergey.zhuravlev.rpodmp.lab2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.zhuravlev.rpodmp.lab2.R;
import com.sergey.zhuravlev.rpodmp.lab2.activity.ArticleActivity;
import com.sergey.zhuravlev.rpodmp.lab2.activity.RequestCode;
import com.sergey.zhuravlev.rpodmp.lab2.client.Client;
import com.sergey.zhuravlev.rpodmp.lab2.client.SimpleCallback;
import com.sergey.zhuravlev.rpodmp.lab2.database.BitmapCache;
import com.sergey.zhuravlev.rpodmp.lab2.database.DatabaseHelper;
import com.sergey.zhuravlev.rpodmp.lab2.model.Article;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.ResponseBody;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> implements DatabaseHelper.DatabaseListener {

    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private final BitmapCache bitmapCache;

    public static class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView image;
        TextView title, time, description;

        Article article;

        ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = itemView.findViewById(R.id.cardView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ArticleActivity.class);
            intent.setAction(RequestCode.ACTION_VIEW_USER);
            intent.putExtra(Article.class.getCanonicalName(), article);
            ((Activity) v.getContext()).startActivityForResult(intent, RequestCode.NONE);
        }

    }

    private DatabaseHelper helper;
    private Context context;

    private List<Article> articles;

    public ArticleAdapter(Context context, DatabaseHelper helper) {
        this.bitmapCache = BitmapCache.getInstance();
        this.articles = helper.getAllArticle();
        this.helper = helper;
        this.context = context;
        helper.addListener(this);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.article = articles.get(position);
        holder.title.setText(articles.get(position).getTitle());
        holder.description.setText(articles.get(position).getDescription());
        holder.time.setText(dateFormat.format(articles.get(position).getPublishedAt()));
        if (holder.article.getUrlToImage() != null) {
            Bitmap bmp = bitmapCache.getBitmapFromMemCache(holder.article.getUrlToImage());
            if (bmp != null) {
                holder.image.setImageBitmap(bmp);
            } else {
                Client.getInstance().imageDownload(holder.article.getUrlToImage(),
                        new SimpleCallback<ResponseBody>() {
                            @Override
                            public void onResponse(ResponseBody response) {
                                Bitmap bmp = BitmapFactory.decodeStream(response.byteStream());
                                bitmapCache.addBitmapToMemoryCache(holder.article.getUrlToImage(), bmp);
                                ((Activity) context).runOnUiThread(() -> holder.image.setImageBitmap(bmp));
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
            }
        }
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.setAction(RequestCode.ACTION_SHOW_ARTICLE);
            intent.putExtra(Article.class.getCanonicalName(), holder.article);
            ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_EDIT_USER);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public void databaseChanged() {
        this.articles = helper.getAllArticle();
        this.notifyDataSetChanged();
    }

}
