package org.example.project2.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

//used for meeting
public class Agenda {
    private Long id;
    private String title;
    private String speaker;
    private LocalDateTime startTime;
    private Integer duration; // 分钟
    private String content;
    public Agenda(Long id, String title, String speaker, LocalDateTime startTime, Integer duration, String content){
        this.id = id;
        this.title = title;
        this.speaker = speaker;
        this.startTime = startTime;
        this.duration = duration;
        this.content = content;
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

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
