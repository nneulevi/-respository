package org.example.project2.entity;

import java.util.List;

public class requestNews {
    private Long id;
    private String title;//key
    private String summary;
    private String content; // 富文本
    private String coverImage;//photo
    private String author;
    private List<String> tags;
    public requestNews(Long id,String title,String summary,String content,String coverImage,String author,List<String> tags){
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.coverImage = coverImage;
        this.author = author;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
