package com.rss.daniel.rss;


import com.rss.daniel.rss.addrss.AddRssContract;
import com.rss.daniel.rss.addrss.AddRssPresenter;
import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssRepository;
import com.rss.daniel.rss.util.BaseSchedulerProvider;
import com.rss.daniel.rss.util.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by danie on 25/05/2017.
 */

public class AddRssPresenterTest {

    private static List<RssUrl> RSS_URLS;

    @Mock
    private AddRssPresenter mAddRssPresenter;

    @Mock
    private RssRepository mRssRepository;

    private BaseSchedulerProvider mSchedulerProvider;

    @Mock
    private AddRssContract.Presenter mPresenter;

    @Mock
    private AddRssContract.View mAddRssView;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
        RSS_URLS = Arrays.asList(new RssUrl("http1"), new RssUrl("http2"), new RssUrl("http3"));
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        mAddRssPresenter = new AddRssPresenter(mRssRepository, mAddRssView, mSchedulerProvider);
        verify(mAddRssView).setPresenter(mAddRssPresenter);
    }

    @Test
    public void AddRssUrl() {
        String url = "url";
        mAddRssPresenter = new AddRssPresenter(mRssRepository, mAddRssView, mSchedulerProvider);
        mAddRssPresenter.addRssUrl(url);
        verify(mRssRepository).saveRssUrl(any(RssUrl.class));
    }

    @Test
    public void loadRssUrlProgressOn() {
        mAddRssPresenter = new AddRssPresenter(mRssRepository, mAddRssView, mSchedulerProvider);
        when(mRssRepository.getRssUrls()).thenReturn(Observable.just(RSS_URLS));
        mAddRssPresenter.loadRssUrls();
        verify(mAddRssView).setLoadingIndicator(true);
    }

    @Test
    public void loadRssUrltoView() {
        mAddRssPresenter = new AddRssPresenter(mRssRepository, mAddRssView, mSchedulerProvider);
        when(mRssRepository.getRssUrls()).thenReturn(Observable.just(RSS_URLS));
        mAddRssPresenter.loadRssUrls();
        verify(mAddRssView).showRssUrls(RSS_URLS);
    }
}
