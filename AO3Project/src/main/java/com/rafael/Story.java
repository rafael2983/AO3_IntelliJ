package com.rafael;

public class Story {
    private String title;
    private String author;
    private String fandom;
    private String summary;
    private String tags;
    private String genre;
    private String fullText;

    public Story(String title, String author, String fandom, String summary, String tags, String genre, String fullText) {
        this.title = title;
        this.author = author;
        this.fandom = fandom;
        this.summary = summary;
        this.tags = tags;
        this.genre = genre;
        this.fullText = fullText;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSummary() { return summary; }
    public String getTags() { return tags; }
    public String getFandom() { return fandom; }
    public String getGenre() { return genre; }
    public String getFullText() { return fullText; }
}
