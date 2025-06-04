package com.example.uap_pam_watchlist.model;

public class Movie {
    private String id;
    private String title;
    private String status;

    public Movie() {}

    public Movie(String title, String status) {
        this.title = title;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
