package com.rss.daniel.rss.data.source.remote;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssDataSource;
import com.rss.daniel.rss.http.ApiService;
import com.rss.daniel.rss.http.RssFeedService;
import com.rss.daniel.rss.http.model.Channel;
import com.rss.daniel.rss.http.model.RSS;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by danie on 27/05/2017.
 */

public class RssRemoteDataSource implements RssDataSource {

    private static RssRemoteDataSource INSTANCE;
    static RssFeedService mXmlAdapterFor;

    @Override
    public Observable<List<Channel.Item>> getRssList(RssUrl rssUrl) {
        return mXmlAdapterFor.getFeed(rssUrl.getUrl())
                .flatMap(new Func1<RSS, Observable<List<Channel.Item>>>() {
                    @Override
                    public Observable<List<Channel.Item>> call(RSS rss) {
                        for (Channel.Item item : rss.getChannel().itemList) {
                            item.fId = rssUrl.getId();
                        }
                        return Observable.from(rss.getChannel().itemList).toList();
                    }
                });
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
