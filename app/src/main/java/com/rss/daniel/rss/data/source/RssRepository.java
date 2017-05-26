package com.rss.daniel.rss.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.local.RssLocalDataSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by danie on 25/05/2017.
 */

public class RssRepository implements RssDataSource {


    @Nullable
    private static RssRepository INSTANCE = null;
    RssDataSource mRssLocalDataSource;
    Map<String, RssUrl> mCachedUrl;


    public static RssRepository getInstance(@NonNull RssDataSource rssLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RssRepository( rssLocalDataSource);
        }
        return INSTANCE;
    }

    public RssRepository(@NonNull RssDataSource rssLocalDataSource) {
        mRssLocalDataSource = rssLocalDataSource;
    }

    @Override
    public void saveRssUrl(RssUrl url) {
        if (url == null) return;
        mRssLocalDataSource.saveRssUrl(url);
        if (mCachedUrl == null) {
            mCachedUrl = new LinkedHashMap<>();
        }
        mCachedUrl.put(url.getId(), url);
    }

    @Override
    public rx.Observable<List<RssUrl>> getRssUrls() {
        return mRssLocalDataSource.getRssUrls();
    }
}
