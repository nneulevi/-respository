package org.example.project2.entity;

import java.time.LocalDateTime;
import java.util.List;

public class News {
    private Long id;
    private String title;//key
    private String summary;
    private String content; // 富文本
    private String coverImage;//photo
    private User author;
    private Integer viewCount;//number of views
    private LocalDateTime publishTime;//when passed,get the local time
    private List<newsTag> tags;
    public News(Long id,String title,String summary,String content,String coverImage,User author,Integer viewCount,LocalDateTime publishTime,List<newsTag> tags){
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.coverImage = coverImage;
        this.author = author;
        this.viewCount = viewCount;
        this.publishTime=publishTime;
        this.tags = tags;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public List<newsTag> getTags() {
        return tags;
    }

    public void setTags(List<newsTag> tags) {
        this.tags = tags;
    }
}
