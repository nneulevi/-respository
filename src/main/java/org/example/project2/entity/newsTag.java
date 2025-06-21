package org.example.project2.entity;

//used for news, news could have several tags
public class newsTag {
    private Long id;
    private String name;
    public newsTag(Long id,String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
