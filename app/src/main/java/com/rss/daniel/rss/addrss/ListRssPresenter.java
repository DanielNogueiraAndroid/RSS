package com.rss.daniel.rss.addrss;

import android.util.Log;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssRepository;
import com.rss.daniel.rss.http.ApiService;
import com.rss.daniel.rss.http.RssFeedService;
import com.rss.daniel.rss.http.model.xml.Channel;
import com.rss.daniel.rss.http.model.xml.RSS;
import com.rss.daniel.rss.util.BaseSchedulerProvider;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    }

    @Override
    public void loadRssContent(RssUrl rssUrl) {
        mCurrentRssUrl = rssUrl;
        mListRssView.setLoadingIndicator(true);
        mSubscriptions.clear();
        Subscription subscription = mRssRepository
                .getRssList(rssUrl)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .doOnTerminate(()->
                        mListRssView.setLoadingIndicator(false))

/*                .subscribe(new Subscriber<RSS>() {
                    @Override
                    public void onCompleted() {
                        Log.d("loadRss", "onCompleted() called");
                    }
                    @Override
                    public void onError(final Throwable e) {
                        Log.d("loadRss", "onError() called with: e = [" + e + "]");
                    }
                    @Override
                    public void onNext(final RSS rss) {
                        Log.d("loadRss", "onNext() called with: rss = [" + rss + "]");
                    }
                });*/

                .subscribe(this::processRssList,
                        throwable -> mListRssView.showLoadingError());
        mSubscriptions.add(subscription);

      /*  loadRss(rssUrl.getUrl());*/
    }

    private void loadRss(String baseURL) {

        Subscription subscription = getRssObservable(baseURL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RSS>() {
                    @Override
                    public void onCompleted() {
                        Log.d("loadRss", "onCompleted() called");
                    }
                    @Override
                    public void onError(final Throwable e) {
                        Log.d("loadRss", "onError() called with: e = [" + e + "]");
                    }
                    @Override
                    public void onNext(final RSS rss) {
                        Log.d("loadRss", "onNext() called with: rss = [" + rss + "]");
                    }
                });
    }

    public Observable<RSS> getRssObservable(String url) {
        RssFeedService xmlAdapterFor = ApiService.createXmlAdapterFor(RssFeedService.class);
        return xmlAdapterFor.getFeed(url);
    }

    @Override
    public void openRssDetails(Channel.Item clickedRss) {

    }

    @Override
    public void loadRssContent(boolean forceUpdate) {
        loadRssContent(mCurrentRssUrl);
    }

    @Override
    public void onStart() {
        loadRssContent(new RssUrl("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml"));
    }

    private void processRssList(RSS rss) {

        mListRssView.showRssList(rss.getChannel().itemList);

        Log.d("loadRss", "loadRssContent() called with: rss = [" + rss + "]");
    }
}
