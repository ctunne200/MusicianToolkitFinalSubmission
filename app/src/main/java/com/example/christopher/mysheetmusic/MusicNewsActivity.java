package com.example.christopher.mysheetmusic;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.christopher.mysheetmusic.R.string.aboutdialog_desc;
import static com.example.christopher.mysheetmusic.R.string.aboutdialog_title;

public class MusicNewsActivity extends AppCompatActivity {
    // Creates Recycler View
    RecyclerView recyclerView;
    String RssUrlMenuItem = "http://www.musicnotes.com/blog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets the content view to
        setContentView(R.layout.activity_music_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instantiate RecyclerView, calling the MusicNewsReadRSS class to collect the information to parse
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        MusicNewsReadRSS musicNewsReadRSS = new MusicNewsReadRSS(this, recyclerView);
        musicNewsReadRSS.execute();
    }

    @Override
    // Creates the overflow menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music_news_menu, menu);
        return true;
    }

    @Override
    // Method called when an item in the overflow menu is clicked
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Switch statement for each option available, allowing additional options to
        // be added in the future
        switch (id) {

            case R.id.action_rssurl:
                // When user clicks this setting, they will be taken to their default browser
                // and the url relating to the RSS feed
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(RssUrlMenuItem)));
                return true;

            case R.id.action_about:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(MusicNewsActivity.this)
                                    .setTitle(aboutdialog_title)
                                    .setMessage(aboutdialog_desc)
                                    .setCancelable(false)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Whatever...
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
