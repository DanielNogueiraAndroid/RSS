package com.rss.daniel.rss;

import com.rss.daniel.rss.addrss.AddRssPresenter;
import com.rss.daniel.rss.addrss.ListRssContract;
import com.rss.daniel.rss.addrss.ListRssPresenter;
import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssRepository;
import com.rss.daniel.rss.http.model.Channel;
import com.rss.daniel.rss.util.BaseSchedulerProvider;
import com.rss.daniel.rss.util.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by danie on 27/05/2017.
 */

public class ListRssPresenterTest {

    private static final java.util.List<Channel.Item> RSS_LIST = Arrays.asList(new Channel.Item("title 1","link1","description1"),new Channel.Item("title 2","link2","description2"));

    private static final String URL="http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml" ;

    @Mock
    private RssRepository mRssRepository;

    @Mock
    private ListRssPresenter mListRssPresenter;

    @Mock
    public ListRssContract.View mListRssView;

    private BaseSchedulerProvider mSchedulerProvider;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        mListRssPresenter = new ListRssPresenter(mRssRepository, mListRssView, mSchedulerProvider);
        verify(mListRssView).setPresenter(mListRssPresenter);
    }

    @Test
    public void loadRssListProgressOn() {
        RssUrl rssUrl = new RssUrl(URL);
        mListRssPresenter = new ListRssPresenter(mRssRepository, mListRssView, mSchedulerProvider);
        when(mRssRepository.getRssList(rssUrl)).thenReturn(Observable.just(RSS_LIST));
        mListRssPresenter.loadRssContent(rssUrl);
        verify(mListRssView).setLoadingIndicator(true);
    }
}
