package com.example.christopher.mysheetmusic.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.christopher.mysheetmusic.R;
import com.example.christopher.mysheetmusic.data.DatabaseDescription.Entry;

public class MySheetsContentProvider extends ContentProvider {
    // used to access the database
    private MySheetsDatabaseHelper dbHelper;

    // UriMatcher helps ContentProvider determine operation to perform
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    // constants used with UriMatcher to determine operation to perform
    private static final int ONE_ENTRY = 1; // manipulate one contact
    private static final int ENTRIES = 2; // manipulate contacts table

    // static block to configure this ContentProvider's UriMatcher
    static {
        // Uri for Entry with the specified id (#)
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                Entry.TABLE_NAME + "/#", ONE_ENTRY);

        // Uri for Contacts table
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Entry.TABLE_NAME, ENTRIES);
    }

    // called when the MySheetsContentProvider is created
    @Override
    public boolean onCreate() {
        // create the MySheetsDatabaseHelper
        dbHelper = new MySheetsDatabaseHelper(getContext());
        return true; // ContentProvider successfully created
    }

    // required method: Not used in this app, so we return null
    @Override
    public String getType(Uri uri) {
        return null;
    }

    // query the database
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        // create SQLiteQueryBuilder for querying entries table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Entry.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_ENTRY: // entry with specified id will be selected
                queryBuilder.appendWhere(
                        DatabaseDescription.Entry._ID + "=" + uri.getLastPathSegment());
                break;
            case ENTRIES: // all entries will be selected
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }

        // execute the query to select one or all entries
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        // configure to watch for content changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // insert a new entry in the database
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newEntryUri = null;

        switch (uriMatcher.match(uri)) {
            case ENTRIES:
                // insert the new entry--success yields new entry's row id
                long rowId = dbHelper.getWritableDatabase().insert(
                        DatabaseDescription.Entry.TABLE_NAME, null, values);

                // if the entry was inserted, create an appropriate Uri;
                // otherwise, throw an exception
                if (rowId > 0) { // SQLite row IDs start at 1
                    newEntryUri = DatabaseDescription.Entry.buildEntryUri(rowId);

                    // notify observers that the database changed
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newEntryUri;
    }

    // update an existing entry in the database
    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int numberOfRowsUpdated; // 1 if update successful; 0 otherwise

        switch (uriMatcher.match(uri)) {
            case ONE_ENTRY:
                // get from the uri the id of entry to update
                String id = uri.getLastPathSegment();

                // update the entry
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        DatabaseDescription.Entry.TABLE_NAME, values, DatabaseDescription.Entry._ID + "=" + id,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri);
        }

        // if changes were made, notify observers that the database changed
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    // delete an existing entry from the database
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ONE_ENTRY:
                // get from the uri the id of entry to update
                String id = uri.getLastPathSegment();

                // delete the entry
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        DatabaseDescription.Entry.TABLE_NAME, DatabaseDescription.Entry._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        // notify observers that the database changed
        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }
}
