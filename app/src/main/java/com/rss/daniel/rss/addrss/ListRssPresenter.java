package com.rss.daniel.rss.addrss;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssRepository;
import com.rss.daniel.rss.http.model.Channel;
import com.rss.daniel.rss.util.BaseSchedulerProvider;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by danie on 27/05/2017.
 */

public class ListRssPresenter implements ListRssContract.Presenter{

    private final ListRssContract.View mListRssView;
    private final RssRepository mRssRepository;
    private final BaseSchedulerProvider mSchedulerProvider;
    private final CompositeSubscription mSubscriptions;
    RssUrl mCurrentRssUrl;

    public ListRssPresenter(RssRepository rssRepository, ListRssContract.View listRssView,
                            BaseSchedulerProvider schedulerProvider) {
        mListRssView = listRssView;
        mRssRepository = rssRepository;
        mSchedulerProvider = schedulerProvider;
        mSubscriptions = new CompositeSubscription();
        listRssView.setPresenter(this);
    }

    @Override
    public void onCreate() {
        loadRssContent(new RssUrl("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml"));
    }

    @Override
    public void loadRssContent(RssUrl rssUrl) {
        mCurrentRssUrl = rssUrl;
        mListRssView.setLoadingIndicator(true);
        Subscription subscription = mRssRepository
                .getRssList(rssUrl)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .doOnTerminate(()->
                        mListRssView.setLoadingIndicator(false))
                .subscribe(this::processRssList,
                        throwable -> mListRssView.showLoadingError());
        mSubscriptions.add(subscription);
    }

    @Override
    public void openRssDetails(Channel.Item clickedRss) {

    }

    @Override
    public void loadRssContent(boolean forceUpdate) {
        loadRssContent(mCurrentRssUrl);
    }

    private void processRssList(List<Channel.Item> items) {
        mListRssView.showRssList(items);
    }
}
