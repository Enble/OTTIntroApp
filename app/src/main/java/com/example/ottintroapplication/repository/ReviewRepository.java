package com.example.ottintroapplication.repository;

import com.example.ottintroapplication.dto.ReviewItem;

import java.util.ArrayList;

public class ReviewRepository {

    /*
        10개 개수 제한
     */
    ArrayList<ReviewItem> items = new ArrayList<>();
    String url;

    /**
     * 파라미터 순서 중요!
     * @param stars
     * @param titles
     * @param usernames
     * @param dates
     * @param contents
     */
    public void initItems(ArrayList<String> stars, ArrayList<String> titles,
                         ArrayList<String> usernames, ArrayList<String> dates,
                         ArrayList<String> contents) {
        setStars(stars);
        setTitles(titles);
        setUsernames(usernames);
        setDates(dates);
        setContents(contents);
    }

    public ArrayList<ReviewItem> getItems() {
        return items;
    }

    public void setStars(ArrayList<String> stars) {
        for (int i=0; i<10; i++) {
            ReviewItem reviewItem = new ReviewItem();
            reviewItem.setStar(stars.get(i));
            items.add(reviewItem);
        }
    }

    public void setTitles(ArrayList<String> titles) {
        for(int i=0; i<10; i++) {
            items.get(i).setTitle(titles.get(i));
        }
    }

    public void setUsernames(ArrayList<String> usernames) {
        for(int i=0; i<10; i++) {
            items.get(i).setUsername(usernames.get(i));
        }
    }

    public void setDates(ArrayList<String> dates) {
        for(int i=0; i<10; i++) {
            items.get(i).setDate(dates.get(i));
        }
    }

    public void setContents(ArrayList<String> contents) {
        for(int i=0; i<10; i++) {
            items.get(i).setContent(contents.get(i));
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void putReview(ReviewItem item) {
        items.remove(0);
        items.add(0, item);
    }
}
