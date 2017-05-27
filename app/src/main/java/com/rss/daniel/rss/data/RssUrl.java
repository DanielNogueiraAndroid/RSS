package com.rss.daniel.rss.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by danie on 25/05/2017.
 */

public class RssUrl {

    @NonNull
    private final String mId;


    @Nullable
    private final String mUrl;

    public RssUrl(@NonNull String mId, String mUrl) {
        this.mId = mId;
        this.mUrl = mUrl;
    }

    public RssUrl(String url) {
       this(UUID.randomUUID().toString(), url);
    }

    public String getId() {
        return mId;
    }

    public String getUrl() {
        return mUrl;
    }
    
    @Override
    public String toString() {
        return "Url " + mUrl;
    }
}
