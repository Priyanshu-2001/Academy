package com.sports.oscaracademy.data;

public class feedsData {
    String date, feed, senderID, feedID;

    String onlyDate;

    public feedsData(String date, String feed, String senderID) {
        this.date = date;
        this.feed = feed;
        this.senderID = senderID;
    }

    public feedsData() {
    }

    public String getOnlyDate() {
        return onlyDate;
    }

    public void setOnlyDate(String onlyDate) {
        this.onlyDate = onlyDate;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
