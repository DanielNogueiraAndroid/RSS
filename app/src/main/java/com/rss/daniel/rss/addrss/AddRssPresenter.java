package com.rss.daniel.rss.addrss;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssRepository;
import com.rss.daniel.rss.util.BaseSchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by danie on 25/05/2017.
 */

public class AddRssPresenter implements AddRssContract.Presenter {

    private final BaseSchedulerProvider mSchedulerProvider;
    AddRssContract.View mAddRssView;
    RssRepository mRssRepository;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public AddRssPresenter(RssRepository rssRepository, AddRssContract.View addRssView, BaseSchedulerProvider schedulerProvider) {
        mAddRssView = addRssView;
        mRssRepository = rssRepository;
        mSchedulerProvider = schedulerProvider;
        mSubscriptions = new CompositeSubscription();
        mAddRssView.setPresenter(this);
    }

    @Override
    public void addRssUrl(String url) {
        mRssRepository.saveRssUrl(new RssUrl(url));

    }

    @Override
    public void loadRssUrls() {
        mAddRssView.setLoadingIndicator(true);
        mSubscriptions.clear();
        Subscription subscription = mRssRepository
                .getRssUrls()
                .flatMap(new Func1<List<RssUrl>, Observable<RssUrl>>() {
                    @Override
                    public Observable<RssUrl> call(List<RssUrl> rssUrls) {
                        return Observable.from(rssUrls);
                    }
                })
                .toList()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .doOnTerminate(() -> {
                    Log.d("loadRssUrls","doOnTerminate");
                })
                .subscribe(

                        this::processRssUrl,

                        throwable -> mAddRssView.showLoadingError(),

                        () -> mAddRssView.setLoadingIndicator(false));
        mSubscriptions.add(subscription);
    }

    @Override
    public void onCreate() {
        loadRssUrls();
    }

    private void processRssUrl(List<RssUrl> rssUrls) {
        if (rssUrls.isEmpty()) {
            mAddRssView.showEmptyRssUrls();
        } else {
            mAddRssView.showRssUrls(rssUrls);
        }
    }
}
