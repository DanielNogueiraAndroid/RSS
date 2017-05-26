package com.rss.daniel.rss.data.source;

import android.support.annotation.NonNull;

import com.rss.daniel.rss.data.RssUrl;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by danie on 25/05/2017.
 */

public interface RssDataSource {

    void saveRssUrl(RssUrl url);

    rx.Observable<List<RssUrl>> getRssUrls();
}
