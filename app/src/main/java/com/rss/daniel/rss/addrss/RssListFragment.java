package com.rss.daniel.rss.addrss;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rss.daniel.rss.R;
import com.rss.daniel.rss.http.model.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danie on 27/05/2017.
 */

public class RssListFragment extends Fragment implements ListRssContract.View {

    ListRssContract.Presenter mAddRssPresenter;

    private RssAdapter mListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new RssAdapter(new ArrayList<>(0), mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rsslist, container, false);

        ListView listView = (ListView) root.findViewById(R.id.rss_list);
        listView.setAdapter(mListAdapter);

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);



        // TODO check if its working
        swipeRefreshLayout.setOnRefreshListener(() -> mAddRssPresenter.loadRssContent(true));

        return root;
    }

    @Override
    public void setPresenter(ListRssContract.Presenter addRssPresenter) {
        mAddRssPresenter =addRssPresenter;
    }

    @Override
    public void setLoadingIndicator(boolean visible) {

    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showEmptyRssUrls() {

    }

    @Override
    public void showRssList(List<Channel.Item> rssContents) {
        mListAdapter.replaceData(rssContents);
    }

     RssItemListener mItemListener = new RssItemListener() {
        @Override
        public void onRssClick(Channel.Item clickedRss) {
            mAddRssPresenter.openRssDetails(clickedRss);
        }
    };

    public static RssListFragment newInstance() {
        return new RssListFragment();
    }

    public interface RssItemListener {

        void onRssClick(Channel.Item clickedTask);
    }

    private static class RssAdapter extends BaseAdapter{

        private List<Channel.Item> items;
        private RssItemListener mRssItemListener;

        public RssAdapter(List<Channel.Item> items, RssItemListener mRssItemListener) {
            this.items = items;
            this.mRssItemListener = mRssItemListener;
        }

        public void replaceData(List<Channel.Item> items){
            setList(items);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Channel.Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                rowView = inflater.inflate(R.layout.rss_item, parent, false);
            }
            final Channel.Item item = getItem(position);

            TextView titleTV = (TextView) rowView.findViewById(R.id.title);
            titleTV.setText(item.title);

            return rowView;
        }

        public void setList(List<Channel.Item> list) {
            this.items = list;
        }
    }
}
