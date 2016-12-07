package com.example.christopher.mysheetmusic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Christopher on 24/11/2016.
 */

public class MusicNewsReadRSS extends AsyncTask<Void, Void, Void> {
    Context context;
    // Creates a string which holds the URL for the RSS feed XML
    String address = "http://www.musicnotes.com/blog/feed/";

    // Creates a progressDialog object
    ProgressDialog progressDialog;

    // Creates an Arraylist of all the items in MusicNewsFeedItem class
    ArrayList <MusicNewsFeedItem> musicNewsFeedItems;

    // Creates a recyclerView to display the RSS items
    RecyclerView recyclerView;

    //Creates a URL object that will reference
    URL url;

    public MusicNewsReadRSS(Context context, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        // sets the progressDialog message to be shown during the onPreExecute method
        progressDialog.setMessage("Loading RSS");

    }

    // Will get triggered before doInBackground method
    @Override
    protected void onPreExecute() {
        // Shows dialog box while the RSS feed is loading
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();

        // Sets the recycler view to hold the data gathered from the MusicNewsRSSAdapter class
        MusicNewsRSSAdapter adapter = new MusicNewsRSSAdapter(context, musicNewsFeedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected Void doInBackground(Void... voids) {
        ProcessXml(Getdata());

        return null;
    }

    // This method processes the XML information gathered from the Document method.
    // It utilises the DOM imports Element and Node, grabbing a a Node channel and creating a
    // NodeList to store the ChildNodes: title, pubDate and link.
    private void ProcessXml(Document data) {
        if (data != null) {
            musicNewsFeedItems = new ArrayList<>();
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i=0; i<items.getLength(); i++){
                Node currentchild=items.item(i);
                if (currentchild.getNodeName().equalsIgnoreCase("item")) {
                    MusicNewsFeedItem item = new MusicNewsFeedItem();
                    NodeList itemchilds=currentchild.getChildNodes();
                    for (int a=0; a<itemchilds.getLength(); a++){
                        Node current=itemchilds.item(a);
                        if (current.getNodeName().equalsIgnoreCase("title")){
                            item.setTitle(current.getTextContent());
                        }
                        /*
                        else if (current.getNodeName().equalsIgnoreCase("description")){
                            item.setDescription(current.getTextContent());
                        }
                        */
                        else if (current.getNodeName().equalsIgnoreCase("pubDate")){
                            item.setPubDate(current.getTextContent());
                        }else if (current.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(current.getTextContent());
                        }
                    }
                    musicNewsFeedItems.add(item);
                    Log.d("itemTitle", item.getTitle());
                    //Log.d("itemDescription", item.getDescription());
                    Log.d("itemLink", item.getLink());
                    Log.d("itemPubDate", item.getPubDate());
                }
            }
        }
    }

    // Document method grabs the data from the URL(address) via an inputstream
    // and parses the data using a DocumentBuilderFactory
    public Document Getdata() {
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
