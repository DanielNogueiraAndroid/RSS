package com.rss.daniel.rss.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by danie on 25/05/2017.
 */

public final class RssPersistenceContract {

    public RssPersistenceContract() {}

    public static abstract class RssEntry implements BaseColumns{
        public static final String TABLE_NAME = "rss";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_URL = "url";
    }
}
