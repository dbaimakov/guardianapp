package com.example.guardianapp.model;

public class Article {
    private long id; // for DB later
    private String title;
    private String url;
    private String section;
    private String publicationDate;

    public Article(long id, String title, String url, String section, String publicationDate) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.section = section;
        this.publicationDate = publicationDate;
    }

    public Article(String title, String url, String section, String publicationDate) {
        this(-1, title, url, section, publicationDate);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getSection() { return section; }
    public String getPublicationDate() { return publicationDate; }

    @Override
    public String toString() {
        return title;
    }
}
