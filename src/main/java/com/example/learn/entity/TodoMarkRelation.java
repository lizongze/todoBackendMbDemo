package com.example.learn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public class TodoMarkRelation {
    private Long id;
    private Long todoId;
    private Long markId;
    private Long userId;
    private LocalDateTime createdAt;

    // 为了兼容联表查询和前端结构
    private TodoMark todoMark;

    public TodoMarkRelation() {}

    public TodoMarkRelation(Long todoId, Long markId) {
        this.todoId = todoId;
        this.markId = markId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTodoId() { return todoId; }
    public void setTodoId(Long todoId) { this.todoId = todoId; }
    public Long getMarkId() { return markId; }
    public void setMarkId(Long markId) { this.markId = markId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public TodoMark getTodoMark() { return todoMark; }
    public void setTodoMark(TodoMark todoMark) { this.todoMark = todoMark; }
}