package com.example.christopher.mysheetmusic.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Christopher on 23/11/2016.
 */

public class DatabaseDescription {
    //Content Provider's name: typically the package name
    public static final String AUTHORITY = "com.example.christopher.mysheetmusic.data";

    //base URI used to interact with the ContentProvider
    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    // nested class defines contents of the contacts table
    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entries"; // table's name

        // Uri for the contacts table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // column names for contacts table's columns
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_COMPOSER = "composer";
        public static final String COLUMN_GENRE = "genre";
        /* public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";
*/
        // creates a Uri for a specific contact
        public static Uri buildEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
