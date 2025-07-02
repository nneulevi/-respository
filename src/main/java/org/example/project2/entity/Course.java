package org.example.project2.entity;

import java.util.List;

public class Course {
    private Long id;
    private String title;
    private String videoUrl;//视频路径
    private String coverImage;//封面路径
    private Integer duration;//持续时间
    private String author;//作者
    private String summary;//总结
    private String content;//内容
    private Integer likes;//点赞数
    private Integer status;
    private List<courseCategory> categories;//分类
    public Course() {}
    public Course(Long id, String title, String videoUrl, String coverImage, Integer duration, String author, String summary,String content, Integer likes, Integer status, List<courseCategory> categories) {
        this.id = id;
        this.title = title;
        this.videoUrl = videoUrl;
        this.coverImage = coverImage;
        this.duration = duration;
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.likes = likes;
        this.status = status;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
