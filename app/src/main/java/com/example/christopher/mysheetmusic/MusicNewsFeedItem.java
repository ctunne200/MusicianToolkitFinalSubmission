package com.example.christopher.mysheetmusic;

/**
 * Created by Christopher on 25/11/2016.
 */
// Stores title, link and pubDate
public class MusicNewsFeedItem {
    // Creates strings for each tag in XML file of RSS feed
    String title;
    String link;
    String pubDate;

    // Getters and setters of each tag in the XML file of RSS feed
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate.substring(0, pubDate.length() - 5);
    }
}
