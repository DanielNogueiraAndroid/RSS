package com.rss.daniel.rss.addrss;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.http.model.Channel;

import java.util.List;

/**
 * Created by danie on 25/05/2017.
 */

public interface ListRssContract {
    public interface View {
        void setPresenter(ListRssContract.Presenter addRssPresenter);

        void setLoadingIndicator(boolean visible);

        void showLoadingError();

        void showEmptyRssUrls();

        void showRssList(List<Channel.Item> rssContents);
    }

    public interface Presenter {

        void onCreate();

        void loadRssContent(RssUrl rssUrl);

        void openRssDetails(Channel.Item clickedRss);

        void loadRssContent(boolean forceUpdate);
    }
}
