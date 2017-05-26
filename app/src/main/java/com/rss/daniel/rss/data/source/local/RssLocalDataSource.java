package com.rss.daniel.rss.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rss.daniel.rss.data.RssUrl;
import com.rss.daniel.rss.data.source.RssDataSource;
import com.rss.daniel.rss.util.BaseSchedulerProvider;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by danie on 25/05/2017.
 */

public class RssLocalDataSource implements RssDataSource {

    @Nullable
    private static RssLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private Func1<Cursor, RssUrl> mRssMapperFunction;

    public static RssLocalDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new RssLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RssLocalDataSource(@NonNull Context context,
                                 @NonNull BaseSchedulerProvider schedulerProvider) {
        RssDbHelper dbHelper = new RssDbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());
        mRssMapperFunction = this::getRssUrl;
    }

    @Override
    public void saveRssUrl(RssUrl rssUrl) {
        if(rssUrl == null)return;
        ContentValues values = new ContentValues();
        values.put(RssPersistenceContract.RssEntry.COLUMN_NAME_ENTRY_ID, rssUrl.getId());
        values.put(RssPersistenceContract.RssEntry.COLUMN_NAME_URL, rssUrl.getUrl());
        mDatabaseHelper.insert(RssPersistenceContract.RssEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public rx.Observable<List<RssUrl>> getRssUrls() {
        String[] projection = {
                RssPersistenceContract.RssEntry.COLUMN_NAME_ENTRY_ID,
                RssPersistenceContract.RssEntry.COLUMN_NAME_URL,

        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), RssPersistenceContract.RssEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(RssPersistenceContract.RssEntry.TABLE_NAME, sql)
                .mapToList(mRssMapperFunction);
    }

    @NonNull
    private RssUrl getRssUrl(@NonNull Cursor c) {
        String itemId = c.getString(c.getColumnIndexOrThrow(RssPersistenceContract.RssEntry.COLUMN_NAME_ENTRY_ID));
        String title = c.getString(c.getColumnIndexOrThrow(RssPersistenceContract.RssEntry.COLUMN_NAME_URL));
        return new RssUrl(title,itemId);
    }

}
