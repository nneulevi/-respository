package org.example.project2.entity;

import java.util.List;

public class Course {
    private Long id;
    private String title;
    private String videoUrl;
    private String coverImage;
    private Integer duration;
    private User author;
    private String content;
    private List<courseCategory> categories;
    public Course(Long id, String title, String videoUrl, String coverImage, Integer duration, User author, String content, List<courseCategory> categories) {
        this.id = id;
        this.title = title;
        this.videoUrl = videoUrl;
        this.coverImage = coverImage;
        this.duration = duration;
        this.author = author;
        this.content = content;
        this.categories = categories;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<courseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<courseCategory> categories) {
        this.categories = categories;
    }

}
