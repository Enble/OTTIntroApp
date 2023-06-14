package com.example.ottintroapplication.dto;

public class ReviewItem {

    String star;
    String title;
    String username;
    String date;
    String content;

    public ReviewItem() {
    }
    public ReviewItem(String star, String title, String username, String date, String content) {
        this.star = star;
        this.title = title;
        this.username = username;
        this.date = date;
        this.content = content;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
