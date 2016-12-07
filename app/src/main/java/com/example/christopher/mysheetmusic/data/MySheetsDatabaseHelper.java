package com.example.christopher.mysheetmusic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.christopher.mysheetmusic.data.DatabaseDescription.Entry;

class MySheetsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AddressBook.db";
    private static final int DATABASE_VERSION = 1;

    // constructor
    public MySheetsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creates the entries table when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the entires table
        final String CREATE_ENTRIES_TABLE =
                "CREATE TABLE " + DatabaseDescription.Entry.TABLE_NAME + "(" +
                        DatabaseDescription.Entry._ID + " integer primary key, " +
                        DatabaseDescription.Entry.COLUMN_TITLE + " TEXT, " +
                        Entry.COLUMN_COMPOSER + " TEXT, " +
                        Entry.COLUMN_GENRE + " TEXT);";
        db.execSQL(CREATE_ENTRIES_TABLE); // create the entries table
    }

    // normally defines how to upgrade the database when the schema changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) { }
}
