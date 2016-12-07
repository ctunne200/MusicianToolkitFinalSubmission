package com.example.christopher.mysheetmusic;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MySheetsActivity extends AppCompatActivity
        implements MySheetsFragment.EntryFragmentListener,
        MySheetsDetailFragment.DetailFragmentListener,
        MySheetsAddEditFragment.AddEditFragmentListener{
    // Key for storing an entry's Uri in a Bundle passed to a fragment
    public static final String ENTRY_URI = "entry_uri";

    private MySheetsFragment mySheetsFragment; // Displays the entry list

    // Displays MySheetsFragment when MainActivity first loads
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sheets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // If layout contains fragmentContainer, the phone layout is in use;
        // Creates and displays a MySheetsFragment
        if (savedInstanceState == null &&
                findViewById(R.id.fragmentContainer) != null) {
            // Create MySheetsFragment
            mySheetsFragment = new MySheetsFragment();

            // Add the fragment to the FrameLayout
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, mySheetsFragment);
            transaction.commit(); // Display MySheetsFragment
        }
        else {
            mySheetsFragment =
                    (MySheetsFragment) getSupportFragmentManager().
                            findFragmentById(R.id.entriesFragment);
        }
    }

    // Displays the MySheetsDetailFragment for selected entry
    @Override
    public void onEntrySelected(Uri entryUri) {
        if (findViewById(R.id.fragmentContainer) != null)
            // For use on a phone display
            displayEntry(entryUri, R.id.fragmentContainer);
        else { // For use on a tablet display
            getSupportFragmentManager().popBackStack();

            displayEntry(entryUri, R.id.rightPaneContainer);
        }
    }

    // Displays the MySheetsAddEditFragment to add a new entry
    @Override
    public void onAddEntry() {
        if (findViewById(R.id.fragmentContainer) != null)
            // For use on a phone display
            displayAddEditFragment(R.id.fragmentContainer, null);
        else // For use on a tablet display
            displayAddEditFragment(R.id.rightPaneContainer, null);
    }

    // Displays an entry
    private void displayEntry(Uri entryUri, int viewID) {
        MySheetsDetailFragment mySheetsDetailFragment = new MySheetsDetailFragment();

        // Specifies entry's Uri as an argument to the MySheetsDetailFragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(ENTRY_URI, entryUri);
        mySheetsDetailFragment.setArguments(arguments);

        // Uses a FragmentTransaction to display the MySheetsDetailFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, mySheetsDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes MySheetsDetailFragment to display
    }

    // Displays fragment for adding a new or editing an existing entry
    private void displayAddEditFragment(int viewID, Uri entryUri) {
        MySheetsAddEditFragment mySheetsAddEditFragment = new MySheetsAddEditFragment();

        // If editing an existing entry, this provides the entryUri as an argument
        if (entryUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ENTRY_URI, entryUri);
            mySheetsAddEditFragment.setArguments(arguments);
        }

        // Uses a FragmentTransaction to display the MySheetsAddEditFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, mySheetsAddEditFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // Causes MySheetsAddEditFragment to display
    }

    // Return to entry list when displayed entry deleted
    @Override
    public void onEntryDeleted() {
        getSupportFragmentManager().popBackStack();
        mySheetsFragment.updateEntryList(); // Refreshes entries
    }

    // Displays the MySheetsAddEditFragment to edit an existing entry
    @Override
    public void onEditEntry(Uri entryUri) {
        if (findViewById(R.id.fragmentContainer) != null)
            // For use on a phone display
            displayAddEditFragment(R.id.fragmentContainer, entryUri);
        else // For use on a tablet display
            displayAddEditFragment(R.id.rightPaneContainer, entryUri);
    }

    // Updates the GUI after new entry or updated entry has been saved
    @Override
    public void onAddEditCompleted(Uri entryUri) {
        getSupportFragmentManager().popBackStack();
        mySheetsFragment.updateEntryList(); // Refresh entries

        if (findViewById(R.id.fragmentContainer) == null) { // tablet
            getSupportFragmentManager().popBackStack();

            // On a tablet, display entry that was just added or edited
            displayEntry(entryUri, R.id.rightPaneContainer);
        }
    }
}



