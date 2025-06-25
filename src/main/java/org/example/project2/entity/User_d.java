package org.example.project2.entity;

import java.time.LocalDate;

public class User_d {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String gender;
    private Integer status;
    private LocalDate creatTime;
    private String avatar_url;
    private Long enterprise;
    public User_d(String username, String password, String phone, String email, String gender, Integer status, LocalDate creatTime, String avatar_url, Long enterprise){
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.status = status;
        this.creatTime = creatTime;
        this.avatar_url = avatar_url;
        this.enterprise = enterprise;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(LocalDate creatTime) {
        this.creatTime = creatTime;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Long getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Long enterprise) {
        this.enterprise = enterprise;
    }
}
