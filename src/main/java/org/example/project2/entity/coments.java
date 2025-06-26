package org.example.project2.entity;

import java.time.LocalDateTime;

public class coments {
    long id;//评论ID，主键，自增
    long news_id;//指向该评论属于哪个新闻
    String content;//评论内容
    LocalDateTime createTime;
    Integer likes;
    String author;
    public coments(long id,long news_id,String content,LocalDateTime createTime,Integer likes,String author){
        this.id = id;
        this.news_id = news_id;
        this.content = content;
        this.createTime = createTime;
        this.likes = likes;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNews_id() {
        return news_id;
    }

    public void setNews_id(long news_id) {
        this.news_id = news_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
