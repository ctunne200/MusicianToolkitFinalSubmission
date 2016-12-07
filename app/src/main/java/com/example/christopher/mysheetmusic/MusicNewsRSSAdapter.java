package com.example.christopher.mysheetmusic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Christopher on 26/11/2016.
 */
// Generates custom row items from the MusicNewsFeedItem Array using MyViewHolder
public class MusicNewsRSSAdapter extends RecyclerView.Adapter<MusicNewsRSSAdapter.MyViewHolder> {

    ArrayList<MusicNewsFeedItem> musicNewsFeedItems;
    Context context;
    public MusicNewsRSSAdapter(Context context, ArrayList<MusicNewsFeedItem> musicNewsFeedItems){
        this.musicNewsFeedItems = musicNewsFeedItems;
        this.context = context;
    }
    // Creates a viewholder object that holds the layout custom_row_news_item cardview
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_news_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MusicNewsFeedItem current = musicNewsFeedItems.get(position);
        holder.Title.setText(current.getTitle());
        holder.Date.setText(current.getPubDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MusicNewsDetailsActivity.class);
                intent.putExtra("Link", current.getLink());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return musicNewsFeedItems.size();
    }

    // Sets up the ViewHolder inside the RecyclerView with the title_text and date_text objects
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Date;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title_text);
            Date = (TextView) itemView.findViewById(R.id.date_text);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
