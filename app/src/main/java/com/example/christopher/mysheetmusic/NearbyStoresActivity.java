package com.example.christopher.mysheetmusic;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearbyStoresActivity extends AppCompatActivity implements OnMapReadyCallback {
    FragmentManager fmAboutDialogue;
    private GoogleMap mMap;
    List<NearbyStoresMapData> mapDataLst;
    private Marker[] mapDataMarkerList = new Marker[5];
    private GoogleMap mapNearbyStores; //Google Map variable
    private float markerColours[] = {210.0f, 120.0f, 300.0f, 330.0f, 270.0f};
    private static final LatLng latlangEKCentre = new LatLng(55.7591402, -
            4.1883331); //The Latitude and Longitude for the centre of East Kilbride

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_stores); // app main UI screen
// Action Bar
        fmAboutDialogue = this.getFragmentManager();
        mapDataLst = new ArrayList<NearbyStoresMapData>();
        NearbyStoresMapDataDB mapDB = new NearbyStoresMapDataDB(this, "musicStores.s3db", null,
                1);
        try {
            mapDB.dbCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapDataLst = mapDB.allMapData();
        SetUpMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapNearbyStores = googleMap;
        if (mapNearbyStores != null) {
            mapNearbyStores.moveCamera(CameraUpdateFactory.newLatLngZoom(latlangEKCentre,
                    12)); //Set the default position to EK
            mapNearbyStores.getUiSettings().setCompassEnabled(true);
//Turn on the Compass
            mapNearbyStores.getUiSettings().setMyLocationButtonEnabled(true);
//Turn on the Location Buttons Functionality
            mapNearbyStores.getUiSettings().setRotateGesturesEnabled(true);
            AddMarkers();
        }
    }
    public void SetUpMap() {
        MapFragment mapStarSignsEK = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map);
        mapStarSignsEK.getMapAsync(this); //Create the map and apply to the variable
    }

    public void AddMarkers() {
        MarkerOptions marker;
        NearbyStoresMapData mapData;
        String mrkTitle;
        String mrkText;
/* For all the marker options in dbList list */
        for (int i = 0; i < mapDataLst.size(); i++) {
            mapData = mapDataLst.get(i);
            mrkTitle = mapData.getStorePostcode() + " " + mapData.getStoreName();
            mrkText = "Postcode: " + mapData.getStorePostcode();
            marker = SetMarker(mrkTitle, mrkText, new LatLng(mapData.getLatitude(),
                    mapData.getLongitude()), markerColours[i], true);
            mapDataMarkerList[i] = mapNearbyStores.addMarker(marker); //create a maker and add to the venue markers list
        }
    }
    public MarkerOptions SetMarker(String title, String snippet, LatLng position,
                                   float markerColour, boolean centreAnchor) {
        float anchorX; //Create anchorX
        float anchorY; //Create anchorY
/* On the condition the anchor is to be centred */
        if (centreAnchor) {
            anchorX = 0.5f; //Centre X
            anchorY = 0.5f; //Centre Y
        } else {
            anchorX = 0.5f; //Centre X
            anchorY = 1f; //Bottom Y
        }
/* create marker options from the input variables */
        MarkerOptions marker = new
                MarkerOptions().title(title).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(markerColour)).anchor(anchorX, anchorY).position(position);
        return marker; //Return marker
    }
}
