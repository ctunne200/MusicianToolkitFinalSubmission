package com.example.christopher.mysheetmusic;

import java.io.Serializable;

/**
 * Created by rla on 03/11/2016.
 */

public class NearbyStoresMapData implements Serializable {
    // *********************************************
// Declare variables etc.
// *********************************************
    private int storeID;
    private String StoreName;
    private String StorePostcode;
    private float Latitude;
    private float Longitude;
    private static final long serialVersionUID = 0L;
    // *********************************************
// Declare getters and setters etc.
// *********************************************
    public int getStoreID() {
        return storeID;
    }
    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }
    public String getStoreName() {
        return StoreName;
    }
    public void setStoreName(String storeName) {
        this.StoreName = storeName;
    }
    public String getStorePostcode() {
        return StorePostcode;
    }
    public void setStorePostcode(String storePostcode) {
        this.StorePostcode = storePostcode;
    }
    public float getLatitude()
    {
        return Latitude;
    }
    public void setLatitude(float Lat)
    {
        this.Latitude = Lat;
    }
    public float getLongitude()
    {
        return Longitude;
    }
    public void setLongitude(float fLongitude)
    {
        this.Longitude = fLongitude;
    }
    @Override
    public String toString() {
        String mapData;
        mapData = "mcStarSignsInfo [storeID=" + storeID;
        mapData = ", StoreName=" + StoreName;
        mapData = ", StorePostcode=" + StorePostcode;
        mapData = ", Latitude=" + Latitude;
        mapData = ", Longitude=" + Longitude +"]";
        return mapData;
    }
}