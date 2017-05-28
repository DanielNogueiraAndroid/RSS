package com.rss.daniel.rss.data.source.remote;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssDataSource;
import com.rss.daniel.rss.http.ApiService;
import com.rss.daniel.rss.http.RssFeedService;
import com.rss.daniel.rss.http.model.xml.RSS;

import rx.Observable;

/**
 * Created by danie on 27/05/2017.
 */

public class RssRemoteDataSource implements RssDataSource {

    private static RssRemoteDataSource INSTANCE;
    static RssFeedService mXmlAdapterFor;

    @Override
    public Observable<RSS> getRssList(RssUrl rssUrl) {
        return mXmlAdapterFor.getFeed(rssUrl.getUrl());
    }

    public static RssRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RssRemoteDataSource();
        }
        return INSTANCE;
    }

    private RssRemoteDataSource() {
        mXmlAdapterFor = ApiService.createXmlAdapterFor(RssFeedService.class);
    }

}
