package com.example.christopher.mysheetmusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rla on 03/11/2016.
 */

public class NearbyStoresMapDataDB extends SQLiteOpenHelper {
    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/com.example.christopher.mysheetmusic/databases/";
    private static final String DB_NAME = "musicStores.s3db";
    private static final String TBL_STORES = "glasgowStores";
    public static final String COL_STOREID = "storeID";
    public static final String COL_STORENAME = "StoreName";
    public static final String COL_STOREPOSTCODE = "StorePostcode";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_LONGITUDE = "Longitude";

    private final Context appContext;

    public NearbyStoresMapDataDB(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STORES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_STORES + "("
                + COL_STOREID + " INTEGER PRIMARY KEY,"
                + COL_STORENAME + " TEXT," + COL_STOREPOSTCODE
                + " TEXT," + COL_LATITUDE + " FLOAT"
                + COL_LONGITUDE + " FLOAT" + ")";
        db.execSQL(CREATE_STORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_STORES);
            onCreate(db);
        }
    }

    /*
    ===============================================================================
            =
// Creates a empty database on the system and rewrites it with your own
    database.
//
    ===============================================================================
            =
    */
    public void dbCreate() throws IOException {
        boolean dbExist = dbCheck();
        if (!dbExist) {
//By calling this method an empty database will be created into the default system path
//of your application so we can overwrite that database with our database.
                        this.getReadableDatabase();
            Log.d("SQLHelper", "Empty database created");
                try {
                    copyDBFromAssets();
                    Log.d("SQLHelper", "Data copied");
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
        }
    }
/*
    ============================================================================================
// Check if the database already exist to avoid re-copying the file each time
you open the application.
// @return true if it exists, false if it doesn't

============================================================================================
    */

    private boolean dbCheck() {

        SQLiteDatabase db = null;
        try {
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);
        } catch (SQLiteException e) {
            Log.e("SQLHelper", "Database not Found!");
        }
        if (db != null) {
            db.close();
        }
        return db != null ? true : false;
    }


    /*
    ===============================================================================
            =============
    // Copies your database from your local assets-folder to the just created empty
            database in the
    // system folder, from where it can be accessed and handled.
    // This is done by transfering bytestream.
    //
            ===============================================================================
            =============
            */
    private void copyDBFromAssets() throws IOException {
        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;
        try {
            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
//transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0) {
                dbOutput.write(buffer, 0, length);
            }
//Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch (IOException e)
        {
            throw new Error("Problems copying DB!");
        }
    }
    public void addaMapStoreEntry(NearbyStoresMapData aMapStores) {
        ContentValues values = new ContentValues();
        values.put(COL_STORENAME, aMapStores.getStoreName());
        values.put(COL_STOREPOSTCODE, aMapStores.getStorePostcode());
        values.put(COL_LATITUDE, aMapStores.getLatitude());
        values.put(COL_LONGITUDE, aMapStores.getLongitude());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TBL_STORES, null, values);
        db.close();
    }
    public NearbyStoresMapData getMapStoreEntry(String aMapStoreEntry) {
        String query = "Select * FROM " + TBL_STORES + " WHERE " + COL_STORENAME +
                " = \"" + aMapStoreEntry + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        NearbyStoresMapData MapDataEntry = new NearbyStoresMapData();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            MapDataEntry.setStoreID(Integer.parseInt(cursor.getString(0)));
            MapDataEntry.setStoreName(cursor.getString(1));
            MapDataEntry.setStorePostcode(cursor.getString(2));
            MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(3)));
            MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(4)));
            cursor.close();
        } else {
            MapDataEntry = null;
        }
        db.close();
        return MapDataEntry;
    }
    /*
    public boolean removeaMapStoreEntry(String aMapStoreEntry) {
        boolean result = false;
        String query = "Select * FROM " + TBL_STORES + " WHERE " + COL_STORENAME +
                " = \"" + aMapStoreEntry + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        mcStarSignsInfo StarSignsInfo = new mcStarSignsInfo();
        if (cursor.moveToFirst()) {
            StarSignsInfo.setStarSignID(Integer.parseInt(cursor.getString(0)));
            db.delete(TBL_STORES, COL_STOREID + " = ?",
                    new String[] { String.valueOf(StarSignsInfo.getStarSignID())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    } */
    public List<NearbyStoresMapData> allMapData()
    {
        String query = "Select * FROM " + TBL_STORES;
        List<NearbyStoresMapData> nearbyStoresMapDataList = new ArrayList<NearbyStoresMapData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast()==false) {
                NearbyStoresMapData MapDataEntry = new NearbyStoresMapData();
                MapDataEntry.setStoreID(Integer.parseInt(cursor.getString(0)));
                MapDataEntry.setStoreName(cursor.getString(1));
                MapDataEntry.setStorePostcode(cursor.getString(2));
                MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(3)));
                MapDataEntry.setLongitude(Float.parseFloat(cursor.getString(4)));
                nearbyStoresMapDataList.add(MapDataEntry);
                cursor.moveToNext();
            }
        } else {
            nearbyStoresMapDataList.add(null);
        }
        db.close();
        return nearbyStoresMapDataList;
    }
}

