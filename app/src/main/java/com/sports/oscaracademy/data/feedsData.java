package com.sports.oscaracademy.data;

import java.util.Date;

public class feedsData {
    String feed, senderID, feedID;
    long sortingTime;
    Date date;
    String onlyDate;


    public feedsData(String feed, String senderID, String feedID, int sortingTime, Date date, String onlyDate) {
        this.feed = feed;
        this.senderID = senderID;
        this.feedID = feedID;
        this.sortingTime = sortingTime;
        this.date = date;
        this.onlyDate = onlyDate;
    }

    public feedsData(Date date, String feed, String senderID) {
        this.date = date;
        this.feed = feed;
        this.senderID = senderID;
    }

    public feedsData(Date date, String feed, String senderID, long sortingTime) {
        this.date = date;
        this.feed = feed;
        this.senderID = senderID;
        this.sortingTime = sortingTime;
    }

    public long getSortingTime() {
        return sortingTime;
    }

    public void setSortingTime(long sortingTime) {
        this.sortingTime = sortingTime;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
