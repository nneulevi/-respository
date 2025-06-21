package org.example.project2.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Meeting {
    private Long id;
    private String title;
    private String coverImage;
    private String address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content; // 富文本
    private User creator;
    private Integer status; // 0-草稿 1-待审核 2-已发布
    private List<Agenda> agendas;

    public Meeting(Long id, String title, String coverImage, String address, LocalDateTime startTime, LocalDateTime endTime, String content, User creator, Integer status, List<Agenda> agendas) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.creator = creator;
        this.status = status;
        this.agendas = agendas;
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Agenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Agenda> agendas) {
        this.agendas = agendas;
    }

}
