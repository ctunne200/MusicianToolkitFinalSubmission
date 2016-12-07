package com.example.christopher.mysheetmusic;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.christopher.mysheetmusic.data.DatabaseDescription;
import com.example.christopher.mysheetmusic.data.DatabaseDescription.Entry;

public class MySheetsDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Callback methods implemented by MainActivity
    public interface DetailFragmentListener {
        void onEntryDeleted(); // Called when a entry is deleted

        // Pass the Uri of entry to edit to the DetailFragmentListener
        void onEditEntry(Uri entryUri);
    }

    private static final int ENTRY_LOADER = 0; // Identifies the Loader

    private DetailFragmentListener listener;
    private Uri entryUri; // Uri of selected entry

    private TextView titleTextView; // Displays entry's title
    private TextView composerTextView; // Displays entry's composer
    private TextView genreTextView; // Displays entry's genre

    // Sets DetailFragmentListener when fragment attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailFragmentListener) context;
    }

    // Removes DetailFragmentListener when fragment detached
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // Called when DetailFragmentListener's view needs to be created
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // This fragment has menu items to display

        // Gets Bundle of arguments then extract the entry's Uri
        Bundle arguments = getArguments();

        if (arguments != null)
            entryUri = arguments.getParcelable(MySheetsActivity.ENTRY_URI);

        // Inflates MySheetsDetailFragment's layout
        View view =
                inflater.inflate(R.layout.fragment_detail, container, false);

        // Gets the EditTexts
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        composerTextView = (TextView) view.findViewById(R.id.composerTextView);
        genreTextView = (TextView) view.findViewById(R.id.genreTextView);

        // Loads the entry
        getLoaderManager().initLoader(ENTRY_LOADER, null, this);
        return view;
    }

    // // Creates the overflow menu this fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }

    // Handle menu item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                listener.onEditEntry(entryUri); // pass Uri to listener
                return true;
            case R.id.action_delete:
                deleteEntry();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Deletes an entry
    private void deleteEntry() {
        // use FragmentManager to display the confirmDelete DialogFragment
        confirmDelete.show(getFragmentManager(), "confirm delete");
    }

    // DialogFragment to confirm deletion of entry
    private final DialogFragment confirmDelete =
            new DialogFragment() {
                // Creates an AlertDialog and return it
                @Override
                public Dialog onCreateDialog(Bundle bundle) {
                    // Creates a new AlertDialog Builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());

                    builder.setTitle(R.string.confirm_title);
                    builder.setMessage(R.string.confirm_message);

                    // Provides an OK button that simply dismisses the dialog
                    builder.setPositiveButton(R.string.button_delete,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialog, int button) {

                                    // use Activity's ContentResolver to invoke
                                    // delete on the AddressBookContentProvider
                                    getActivity().getContentResolver().delete(
                                            entryUri, null, null);
                                    listener.onEntryDeleted(); // notify listener
                                }
                            }
                    );

                    builder.setNegativeButton(R.string.button_cancel, null);
                    return builder.create(); // Return the AlertDialog
                }
            };

    // Called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;

        switch (id) {
            case ENTRY_LOADER:
                cursorLoader = new CursorLoader(getActivity(),
                        entryUri, // Uri of entry to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    // Called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // If the entry exists in the database, display its data
        if (data != null && data.moveToFirst()) {
            // Gets the column index for each data item
            int titleIndex = data.getColumnIndex(Entry.COLUMN_TITLE);
            int composerIndex = data.getColumnIndex(Entry.COLUMN_COMPOSER);
            int genreIndex = data.getColumnIndex(DatabaseDescription.Entry.COLUMN_GENRE);

            // Fills TextViews with the retrieved data
            titleTextView.setText(data.getString(titleIndex));
            composerTextView.setText(data.getString(composerIndex));
            genreTextView.setText(data.getString(genreIndex));
        }
    }

    // Called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}



