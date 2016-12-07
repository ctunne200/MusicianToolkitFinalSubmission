package com.example.christopher.mysheetmusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.christopher.mysheetmusic.data.DatabaseDescription;
import com.example.christopher.mysheetmusic.data.DatabaseDescription.Entry;

public class MySheetsAddEditFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Defines callback method implemented by MainActivity
    public interface AddEditFragmentListener {
        // Called when contact is saved
        void onAddEditCompleted(Uri entryUri);
    }

    // Constant used to identify the Loader
    private static final int ENTRY_LOADER = 0;

    private AddEditFragmentListener listener; // MainActivity
    private Uri entryUri; // Uri of selected contact
    private boolean addingNewEntry = true; // adding (true) or editing

    // EditTexts for contact information
    private TextInputLayout titleTextInputLayout;
    private TextInputLayout composerTextInputLayout;
    private TextInputLayout genreTextInputLayout;
    private FloatingActionButton saveEntryFAB;

    private CoordinatorLayout coordinatorLayout; // Used with SnackBar messages

    // Set AddEditFragmentListener when Fragment attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddEditFragmentListener) context;
    }

    // Removes AddEditFragmentListener when Fragment is detached
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // Called when Fragment's view needs to be created
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // Fragment has menu items to display

        // Inflate GUI and get references to EditTexts
        View view =
                inflater.inflate(R.layout.fragment_add_edit, container, false);
        titleTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.titleTextInputLayout);
        titleTextInputLayout.getEditText().addTextChangedListener(
                nameChangedListener);
        composerTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.genreTextInputLayout);
        genreTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.composerTextInputLayout);

        // Set FloatingActionButton's event listener
        saveEntryFAB = (FloatingActionButton) view.findViewById(
                R.id.saveFloatingActionButton);
        saveEntryFAB.setOnClickListener(saveEntryButtonClicked);
        updateSaveButtonFAB();

        // Used to display SnackBars with brief messages
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(
                R.id.coordinatorLayout);

        Bundle arguments = getArguments(); // Null if creating new contact

        if (arguments != null) {
            addingNewEntry = false;
            entryUri = arguments.getParcelable(MySheetsActivity.ENTRY_URI);
        }

        // If editing an existing contact, create Loader to get the contact
        if (entryUri != null)
            getLoaderManager().initLoader(ENTRY_LOADER, null, this);

        return view;
    }

    // Detects when the text in the titleTextInputLayout's EditText changes
    // to hide or show saveButtonFAB
    private final TextWatcher nameChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {}

        // Called when the text in titleTextInputLayout changes
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            updateSaveButtonFAB();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    // Shows saveButtonFAB only if the title field is not empty
    private void updateSaveButtonFAB() {
        String input =
                titleTextInputLayout.getEditText().getText().toString();

        // If there is a name title for the entry, show the FloatingActionButton
        if (input.trim().length() != 0)
            saveEntryFAB.show();
        else
            saveEntryFAB.hide();
    }

    // Responds to event generated when user saves an entry
    private final View.OnClickListener saveEntryButtonClicked =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hides the virtual keyboard
                    ((InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getView().getWindowToken(), 0);
                    saveEntry();
                    // Calls the saveEntry method which saves the
                    // entry information to the database
                }
            };

    // Saves entry information to the database
    private void saveEntry() {
        // create ContentValues object containing entry's key-value pairs
        ContentValues contentValues = new ContentValues();
        contentValues.put(Entry.COLUMN_TITLE,
                titleTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Entry.COLUMN_COMPOSER,
                composerTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Entry.COLUMN_GENRE,
                genreTextInputLayout.getEditText().getText().toString());

        if (addingNewEntry) {
            // Use Activity's ContentResolver to invoke
            // insert on the MySheetsContentProvider
            Uri newEntryUri = getActivity().getContentResolver().insert(
                    DatabaseDescription.Entry.CONTENT_URI, contentValues);

            if (newEntryUri != null) {
                Snackbar.make(coordinatorLayout,
                        R.string.entry_added, Snackbar.LENGTH_LONG).show();
                listener.onAddEditCompleted(newEntryUri);
            }
            else {
                Snackbar.make(coordinatorLayout,
                        R.string.entry_not_added, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            int updatedRows = getActivity().getContentResolver().update(
                    entryUri, contentValues, null, null);

            if (updatedRows > 0) {
                listener.onAddEditCompleted(entryUri);
                Snackbar.make(coordinatorLayout,
                        R.string.entry_updated, Snackbar.LENGTH_LONG).show();
            }
            else {
                Snackbar.make(coordinatorLayout,
                        R.string.entry_not_updated, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        switch (id) {
            case ENTRY_LOADER:
                return new CursorLoader(getActivity(),
                        entryUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
            default:
                return null;
        }
    }

    // Called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // If the entry exists in the database, this displays its data
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int titleIndex = data.getColumnIndex(Entry.COLUMN_TITLE);
            int composerIndex = data.getColumnIndex(Entry.COLUMN_COMPOSER);
            int genreIndex = data.getColumnIndex(Entry.COLUMN_GENRE);

            // Fill EditTexts with the retrieved data
            titleTextInputLayout.getEditText().setText(
                    data.getString(titleIndex));
            composerTextInputLayout.getEditText().setText(
                    data.getString(composerIndex));
            genreTextInputLayout.getEditText().setText(
                    data.getString(genreIndex));

            updateSaveButtonFAB();
        }
    }

    // Called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}