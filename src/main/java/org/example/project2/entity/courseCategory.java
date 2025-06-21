package org.example.project2.entity;

//used for course
public class courseCategory {
    private Long id;
    private String name;
    public courseCategory(Long id,String name){
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
