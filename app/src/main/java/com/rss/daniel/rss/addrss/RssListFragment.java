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
import com.rss.daniel.rss.http.model.xml.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danie on 27/05/2017.
 */

public class RssListFragment extends Fragment implements ListRssContract.View {

    ListRssContract.Presenter mAddRssPresenter;

    private RssAdapter mListAdapter;

    TextView textViewStatusList;

    // Set up progress indicator
     ScrollChildSwipeRefreshLayout mSwipeRefreshLayout;

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
        textViewStatusList =( TextView) root.findViewById(R.id.text_list_status);
        ListView listView = (ListView) root.findViewById(R.id.rss_list);
        listView.setAdapter(mListAdapter);

        mSwipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        mSwipeRefreshLayout.setScrollUpChild(listView);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mAddRssPresenter.loadRssContent(true));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAddRssPresenter.onStart();
    }

    @Override
    public void setPresenter(ListRssContract.Presenter addRssPresenter) {
        mAddRssPresenter =addRssPresenter;
    }

    @Override
    public void setLoadingIndicator(boolean visible) {
        textViewStatusList.setVisibility(View.INVISIBLE);
        if( visible ){
            if(!mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(true);
            }
        }else {
            textViewStatusList.setVisibility(View.INVISIBLE);
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void showLoadingError() {
        textViewStatusList.setVisibility(View.VISIBLE);
        textViewStatusList.setText("Sorry error try again");
    }

    @Override
    public void showEmptyRss() {
        textViewStatusList.setVisibility(View.VISIBLE);
        textViewStatusList.setText("Empty");
    }

    @Override
    public void showRssList(List<Channel.Item> rssContents) {
        textViewStatusList.setVisibility(View.INVISIBLE);
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

            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView date = (TextView) rowView.findViewById(R.id.date);
            TextView description = (TextView) rowView.findViewById(R.id.text_description);
            date.setText(item.pubDate);
            title.setText(item.title);
            description.setText(item.title);

            return rowView;
        }

        public void setList(List<Channel.Item> list) {
            this.items = list;
        }
    }
}
