package com.example.christopher.mysheetmusic;


import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.christopher.mysheetmusic.data.DatabaseDescription.Entry;

public class MySheetsAdapter
        extends RecyclerView.Adapter<MySheetsAdapter.ViewHolder> {

    // Interface implemented by MySheetsFragment to respond
    // when the user touches an item in the RecyclerView
    public interface EntryClickListener {
        void onClick(Uri entryUri);
    }

    // Nested subclass of RecyclerView.ViewHolder used to implement
    // the view-holder pattern in the context of a RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        private long rowID;


        // Configures the RecyclerView item's ViewHolder
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
            // Attach listener to itemView
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        // Executes when the entry in this ViewHolder is clicked
                        @Override
                        public void onClick(View view) {
                            clickListener.onClick(Entry.buildEntryUri(rowID));
                        }
                    }
            );
        }

        // Set the database row ID for the entry in this ViewHolder
        public void setRowID(long rowID) {
            this.rowID = rowID;
        }
    }

    // MySheetsAdapter instance variables
    private Cursor cursor = null;
    private final EntryClickListener clickListener;

    // Constructor
    public MySheetsAdapter(EntryClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // Sets up new list item and its ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates the android.R.layout.simple_list_item_1 layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view); // return current item's ViewHolder
    }

    // Sets the text of the list item to display the title
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(Entry._ID)));
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(
                Entry.COLUMN_TITLE)));
    }

    // Returns the number of items that adapter binds
    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    // Swap this adapter's current Cursor for a new one
    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}

