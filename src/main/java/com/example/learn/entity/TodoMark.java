package com.example.learn.entity;

public class TodoMark {
    private Long id;
    private Long userId;
    private String name;
    private String color;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }


    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}