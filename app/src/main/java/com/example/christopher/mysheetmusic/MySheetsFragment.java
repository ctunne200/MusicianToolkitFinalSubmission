package com.example.christopher.mysheetmusic;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.christopher.mysheetmusic.data.DatabaseDescription;

public class MySheetsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Callback method implemented by MainActivity
    public interface EntryFragmentListener {
        // Called when an entry is selected
        void onEntrySelected(Uri entryUri);

        // Called when the add button is pressed
        void onAddEntry();
    }

    private static final int ENTRIES_LOADER = 0; // identifies Loader

    // Used to inform the MainActivity when a entry is selected
    private EntryFragmentListener listener;

    private MySheetsAdapter mySheetsAdapter; // Adapter for RecyclerView

    // Configures this fragment's GUI
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // fragment has menu items to display

        // Inflates GUI and gets reference to the RecyclerView
        View view = inflater.inflate(
                R.layout.fragment_mysheets, container, false);
        RecyclerView recyclerView =
                (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity().getBaseContext()));

        // Creates recyclerView's adapter and item click listener
        mySheetsAdapter = new MySheetsAdapter(
                new MySheetsAdapter.EntryClickListener() {
                    @Override
                    public void onClick(Uri entryUri) {
                        listener.onEntrySelected(entryUri);
                    }
                }
        );
        recyclerView.setAdapter(mySheetsAdapter); // set the adapter

        // Attaches a custom ItemDecorator from ItemDivider class to draw dividers between list items
        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        // Sets the RecyclerView's Layout size if it never changes, improving performance
        recyclerView.setHasFixedSize(true);

        // Gets the FloatingActionButton and configures its listener
        FloatingActionButton addButton =
                (FloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(
                new View.OnClickListener() {
                    // displays the MySheetsAddEditFragment when FAB is touched
                    @Override
                    public void onClick(View view) {
                        listener.onAddEntry();
                    }
                }
        );

        return view;
    }

    // Sets EntryFragmentListener when fragment attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (EntryFragmentListener) context;
    }

    // Removes EntryFragmentListener when Fragment detached
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // Initializes a Loader when this fragment's activity is created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ENTRIES_LOADER, null, this);
    }

    // Called from MainActivity when other Fragment's update database
    public void updateEntryList() {
        mySheetsAdapter.notifyDataSetChanged();
    }

    // Called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Create an appropriate CursorLoader based on the id argument
        switch (id) {
            case ENTRIES_LOADER:
                return new CursorLoader(getActivity(),
                        DatabaseDescription.Entry.CONTENT_URI, // Uri of contacts table
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        DatabaseDescription.Entry.COLUMN_TITLE + " COLLATE NOCASE ASC"); // sort order
            default:
                return null;
        }
    }

    // Called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mySheetsAdapter.swapCursor(data);
    }

    // Called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mySheetsAdapter.swapCursor(null);
    }
}

