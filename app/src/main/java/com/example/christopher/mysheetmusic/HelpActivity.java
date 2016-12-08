package com.example.christopher.mysheetmusic;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.christopher.mysheetmusic.R.string.aboutdialog_desc;
import static com.example.christopher.mysheetmusic.R.string.aboutdialog_title;

public class HelpActivity extends AppCompatActivity {

    HelpExpListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Gets the ListView
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new HelpExpListAdapter(this, listDataHeader, listDataChild);

        // Sets list adapter
        expListView.setAdapter(listAdapter);


    }
    @Override
    // Creates the overflow menu for HelpActivity
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
                            new AlertDialog.Builder(HelpActivity.this)
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

     // Prepares the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adds header data, referencing the strings in strings.xml
        listDataHeader.add(getString(R.string.help_features_head));
        listDataHeader.add(getString(R.string.help_my_sheets_head));
        listDataHeader.add(getString(R.string.help_add_sheets_head));
        listDataHeader.add(getString(R.string.help_edit_sheets_head));
        listDataHeader.add(getString(R.string.help_music_news_head));


        // Adds the child data, referencing the strings in strings.xml
        List<String> helpFeatures = new ArrayList<String>();
        helpFeatures.add(getString(R.string.help_features_desc));

        List<String> helpMySheets = new ArrayList<String>();
        helpMySheets.add(getString(R.string.help_my_sheets_desc));

        List<String> helpAddingSheets = new ArrayList<String>();
        helpAddingSheets.add(getString(R.string.help_add_sheets_desc));

        List<String> helpEditSheets = new ArrayList<String>();
        helpEditSheets.add(getString(R.string.help_edit_sheets_desc));

        List<String> helpMusicNews = new ArrayList<String>();
        helpMusicNews.add(getString(R.string.help_music_news_desc));

        listDataChild.put(listDataHeader.get(0), helpFeatures); // Header, Child data
        listDataChild.put(listDataHeader.get(1), helpMySheets);
        listDataChild.put(listDataHeader.get(2), helpAddingSheets);
        listDataChild.put(listDataHeader.get(3), helpEditSheets);
        listDataChild.put(listDataHeader.get(4), helpMusicNews);
    }

}
