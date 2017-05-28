package com.rss.daniel.rss.http;

import com.rss.daniel.rss.http.model.RSS;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by https://gist.github.com/macsystems/01d7e80554efd344b1f9
 */

public interface RssFeedService {
    /**
     * Provides RSS feed data.
     * @param feedUrl RSS Feed URL. Note: This must be fully qualified URL.
     * @return RSS Feed
     */
    @GET
    Observable<RSS> getFeed(@Url String feedUrl);
}