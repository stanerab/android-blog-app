package com.example.offlineblog.model;

public class Post {

    private long id;
    private String title;
    private String content;
    private String imageUri; // gallery image URI
    private long createdAt;
    private long updatedAt;
    private int uploadStatus; // 0=pending, 1=uploaded, 2=failed

    public Post() {}

    public Post(long id, String title, String content, String imageUri,
                long createdAt, long updatedAt, int uploadStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.uploadStatus = uploadStatus;
    }

    // ===== GETTERS & SETTERS =====

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
