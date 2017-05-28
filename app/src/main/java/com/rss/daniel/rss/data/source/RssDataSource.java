package com.rss.daniel.rss.data.source;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.http.model.Channel;

import java.util.List;


/**
 * Created by daniel on 25/05/2017.
 */

public interface RssDataSource {

    rx.Observable<List<Channel.Item>> getRssList(RssUrl rssUrl);

    public interface Local extends RssDataSource{

        void saveRssUrl(RssUrl url);

        rx.Observable<List<RssUrl>> getRssUrls();

    }
}
