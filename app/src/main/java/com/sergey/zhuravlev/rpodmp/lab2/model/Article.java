package com.sergey.zhuravlev.rpodmp.lab2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article implements Parcelable {

    private Integer id;

    private String author;
    private String description;
    private String title;
    private String url;
    private String urlToImage;
    private Date publishedAt;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeSerializable(publishedAt);
    }

    protected Article(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        author = in.readString();
        description = in.readString();
        title = in.readString();
        url = in.readString();
        urlToImage = in.readString();
        publishedAt = (Date) in.readSerializable();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

}
