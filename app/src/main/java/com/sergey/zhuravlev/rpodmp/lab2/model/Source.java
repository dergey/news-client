package com.sergey.zhuravlev.rpodmp.lab2.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.versionedparcelable.ParcelField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Source implements Parcelable {

    private Integer id;

    private String apiId;
    private String name;
    private String description;
    private String url;
    private String category;

    private Language language;
    private Country country;

    private String small;   // The URL to a small (width 200px) image of the source's logo.
    private String medium;  // The URL to a medium (width 400px) image of the source's logo.
    private String large;   // The URL to a large (width 600px) image of the source's logo.

    private List<String> sortBysAvailable;


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
        dest.writeString(apiId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(category);
        dest.writeString(language.getCode());
        dest.writeString(country.getCode());
        dest.writeString(small);
        dest.writeString(medium);
        dest.writeString(large);
    }

    protected Source(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        apiId = in.readString();
        name = in.readString();
        description = in.readString();
        url = in.readString();
        category = in.readString();
        language = new Language(in.readString());
        country = new Country(in.readString());
        small = in.readString();
        medium = in.readString();
        large = in.readString();
    }

    public static final Parcelable.Creator<Source> CREATOR = new Parcelable.Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

}
