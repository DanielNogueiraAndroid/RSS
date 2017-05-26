package com.rss.daniel.rss.addrss;

import android.support.annotation.NonNull;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssRepository;
import com.rss.daniel.rss.util.BaseSchedulerProvider;

import org.reactivestreams.Subscription;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by danie on 25/05/2017.
 */

public class AddRssPresenter implements AddRssContract.Presenter {

    private final BaseSchedulerProvider mSchedulerProvide;
    AddRssContract.View mAddRssView;
    RssRepository mRssRepository;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public AddRssPresenter(RssRepository rssRepository, AddRssContract.View addRssView, BaseSchedulerProvider schedulerProvider) {
        mAddRssView = addRssView;
        mAddRssView.setPresenter(this);
        mRssRepository = rssRepository;
        mSchedulerProvide = schedulerProvider;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void addRssUrl(String url) {
        mRssRepository.saveRssUrl(new RssUrl(url));
    }

    @Override
    public void loadRssUrls() {
        mAddRssView.setLoadingIndicator(true);
        mRssRepository.getRssUrls();
        mSubscriptions.clear();
        rx.Subscription subscription = mRssRepository
                .getRssUrls()
                .flatMap(new Func1<List<RssUrl>, Observable<RssUrl>>() {
                    @Override
                    public Observable<RssUrl> call(List<RssUrl> rssUrls) {
                        return Observable.from(rssUrls);
                    }
                })
                .toList()
                .subscribeOn(mSchedulerProvide.computation())
                .observeOn(mSchedulerProvide.ui())
                .subscribe(this::processRssUrl,
                        throwable -> mAddRssView.showLoadingError(),
                        () -> mAddRssView.setLoadingIndicator(false));
        mSubscriptions.add(subscription);

    }

    private void processRssUrl(List<RssUrl> rssUrls) {
        if(rssUrls.isEmpty()){
            mAddRssView.showEmptyRssUrls();
        }else {
            mAddRssView.showRssUrls(rssUrls);
        }
    }
}
