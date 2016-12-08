package com.example.christopher.mysheetmusic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static com.example.christopher.mysheetmusic.R.string.aboutdialog_desc;
import static com.example.christopher.mysheetmusic.R.string.aboutdialog_title;
import static com.example.christopher.mysheetmusic.R.string.under_dev_desc;
import static com.example.christopher.mysheetmusic.R.string.under_dev_title;

public class MainActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        // Creates buttons to access other activities
        Button mySheetsButton;
        Button musicNewsButton;
        Button nearbyStoresButton;
        Button helpButton;

        // Relates the buttons to their respective XML ID objects
        mySheetsButton = (Button) findViewById(R.id.MySheetsButton);
        musicNewsButton = (Button) findViewById(R.id.MusicNewsButton);
        nearbyStoresButton = (Button) findViewById(R.id.NearbyStoresButton);
        helpButton = (Button) findViewById(R.id.HelpButton);

        // Instantiates the toolbar to allow the overflow menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar ccActionBar = getSupportActionBar();

        if (ccActionBar != null)
        {
            ccActionBar.setDisplayShowHomeEnabled(true);
            ccActionBar.setLogo(R.mipmap.ic_launcher);
            ccActionBar.setDisplayUseLogoEnabled(true);
        }

        // Makes MySheets Button clickable
        mySheetsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Starts MySheetsActivity when the mySheetsButton is clicked
                Intent mySheetsIntent = new Intent(MainActivity.this,
                        MySheetsActivity.class);
                startActivity(mySheetsIntent);
            }
        });

        // Makes MusicNews Button clickable
        musicNewsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Starts MusicNewsActivity when the musicNewsButton is clicked
                Intent musicNewsIntent = new Intent(MainActivity.this,
                        MusicNewsActivity.class);
                startActivity(musicNewsIntent);
            }
        });

        // Makes NearbyStores Button clickable
        nearbyStoresButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()){
                            // Creates an Alert Dialog, setting the title and message to their respective string values
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(under_dev_title)
                                    .setMessage(under_dev_desc)
                                    .setCancelable(false)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create().show();
                        }
                    }
                });
            }
        });

        // Makes Help Button clickable
        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Starts HelpActivity when the helpButton is clicked
                Intent helpIntent = new Intent(MainActivity.this,
                        HelpActivity.class);
                startActivity(helpIntent);
            }
        });

    }

    @Override
    // Creates the overflow menu for MainActivity
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    // Handle menu item selections
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Switch statement for each option available, allowing additional options to be added in the future
        switch (id) {
            case R.id.action_about:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()){
                            // Creates an Alert Dialog, setting the title and message to their respective string values
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(aboutdialog_title)
                                    .setMessage(aboutdialog_desc)
                                    .setCancelable(false)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create().show();
                        }
                    }
                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}




