package com.rss.daniel.rss.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.http.model.xml.Channel;
import com.rss.daniel.rss.http.model.xml.RSS;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by danie on 25/05/2017.
 */

public class RssRepository implements RssDataSource,RssDataSource.Local {


    @Nullable
    private static RssRepository INSTANCE = null;
    RssDataSource.Local mRssLocalDataSource;
    RssDataSource mRssRemoteDataSource;
    Map<String, RssUrl> mCachedUrl;
    Map<String, List<Channel.Item>> mCachedRss;


    public static RssRepository getInstance(@NonNull RssDataSource.Local rssLocalDataSource,@NonNull RssDataSource rssRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RssRepository(rssLocalDataSource,rssRemoteDataSource);
        }
        return INSTANCE;
    }

    private RssRepository(@NonNull RssDataSource.Local rssLocalDataSource,@NonNull RssDataSource rssRemoteDataSource) {
        mRssLocalDataSource = rssLocalDataSource;
        mRssRemoteDataSource= rssRemoteDataSource;
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
        if (mCachedUrl == null) {
            mCachedUrl = new LinkedHashMap<>();
        }
        return mRssLocalDataSource.getRssUrls()
                .flatMap(new Func1<List<RssUrl>, Observable<List<RssUrl>>>() {
                    @Override
                    public Observable<List<RssUrl>> call(List<RssUrl> rssUrls) {
                        return Observable.from(rssUrls)
                                .doOnNext(rssUrl -> mCachedUrl.put(rssUrl.getId(), rssUrl))
                                .toList();
                    }
                });
    }

    @Override
    public rx.Observable<RSS> getRssList(RssUrl rssUrl) {
        return mRssRemoteDataSource.getRssList(rssUrl);
    }
}
