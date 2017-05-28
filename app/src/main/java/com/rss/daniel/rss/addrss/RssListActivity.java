package com.rss.daniel.rss.addrss;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.rss.daniel.rss.Injection;
import com.rss.daniel.rss.R;
import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.util.ActivityUtils;

import java.lang.reflect.Array;
import java.nio.channels.Channel;
import java.util.List;

public class RssListActivity extends AppCompatActivity implements AddRssContract.View {

    AddRssContract.Presenter mAddRssPresenter;
    ListRssContract.Presenter mLisPresenter;
    private ListView mDrawerList;
    private String[] empty;
    private ArrayAdapter<RssUrl> mAdapterRssUrls;
    DrawerLayout mDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsslist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddRssPresenter = new AddRssPresenter(
                Injection.provideRssRepository(getApplicationContext()),
                this,
                Injection.provideSchedulerProvider());

        RssListFragment tasksFragment =
                (RssListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = RssListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }

        // Create the presenter
        mLisPresenter = new ListRssPresenter(
                Injection.provideRssRepository(getApplicationContext()),
                tasksFragment,
                Injection.provideSchedulerProvider());



        empty = getResources().getStringArray(R.array.empty_array);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mAddRssPresenter.onCreate();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                showDialog()
        );

/*        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }


    @Override
    public void onBackPressed() {

        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rsslist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setPresenter(AddRssContract.Presenter addRssPresenter) {
        mAddRssPresenter = addRssPresenter;
    }

    @Override
    public void setLoadingIndicator(boolean visible) {
        // Toast.makeText(this,"setLoadingIndicator"+ visible,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingError() {
        // Toast.makeText(this,"showLoadingError",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyRssUrls() {

    }

    @Override
    public void showRssUrls(List<RssUrl> rssUrlList) {
        if(mAdapterRssUrls == null){
            mAdapterRssUrls =   new ArrayAdapter<RssUrl>(this,
                    R.layout.drawer_list_item, rssUrlList);
            mDrawerList.setAdapter(mAdapterRssUrls);
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        }else{
            mAdapterRssUrls.clear();
            mAdapterRssUrls.addAll(rssUrlList);
            mAdapterRssUrls.notifyDataSetChanged();
        }
    }

    public void showDialog(){
        LayoutInflater li = LayoutInflater.from(RssListActivity.this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RssListActivity.this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setHint("Please typ the rss url");
        //userInput.setText("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
        userInput.setText("http://feeds.bbci.co.uk/news/world/rss.xml");//TODO remove

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mAddRssPresenter.addRssUrl(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        RssUrl rssUrl  =  mAdapterRssUrls.getItem(position);
        mLisPresenter.loadRssContent(rssUrl);
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }
}
