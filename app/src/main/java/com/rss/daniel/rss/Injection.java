package com.rss.daniel.rss;

import android.content.Context;

import com.rss.daniel.rss.data.source.RssRepository;
import com.rss.daniel.rss.data.source.local.RssLocalDataSource;
import com.rss.daniel.rss.util.BaseSchedulerProvider;
import com.rss.daniel.rss.util.SchedulerProvider;

/**
 * Created by danie on 26/05/2017.
 */

public class Injection {

    public static RssRepository provideRssRepository(Context context) {
        return RssRepository.getInstance(RssLocalDataSource.getInstance(context, provideSchedulerProvider()));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
