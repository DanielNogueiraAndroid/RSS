package com.rss.daniel.rss.addrss;

import com.rss.daniel.rss.data.RssUrl;

import java.util.List;

/**
 * Created by danie on 25/05/2017.
 */

public interface AddRssContract {
    public interface View {
        void setPresenter(AddRssContract.Presenter addRssPresenter);

        void setLoadingIndicator(boolean visible);

        void showLoadingError();

        void showEmptyRssUrls();

        void showRssUrls(List<RssUrl> rssUrls);
    }

    public interface Presenter {

        void addRssUrl(String url);

        void loadRssUrls();

        void onCreate();
    }
}
