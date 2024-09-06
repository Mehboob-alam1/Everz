package com.example.everz;

public class Blog {
    private String title;
    private String description;
    private String imageUrl;
    private String pushId;

    // Default constructor required for calls to DataSnapshot.getValue(Blog.class)
    public Blog() {
    }

    public Blog(String title, String description, String imageUrl, String pushId) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.pushId = pushId;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
