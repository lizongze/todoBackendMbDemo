package com.example.learn.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Todo {

  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  private String title;
  private Boolean completed;
  private String description;
  private String status;
  private String tags;

  private Long userId;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime plannedFinishTime;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private String[] reminders;

  private List<TodoMarkRelation> markRelations;

  @JsonProperty("todoMarkIds")
  public void setTodoMarkIds(List<Long> todoMarkIds) {
    if (todoMarkIds != null) {
      if (this.markRelations == null) {
        this.markRelations = new ArrayList<>();
      }
      this.markRelations.clear();

      todoMarkIds.forEach(markId -> {
        TodoMark mark = new TodoMark();
        mark.setId(markId);
        TodoMarkRelation relation = new TodoMarkRelation(this.id, mark.getId());
        relation.setTodoMark(mark);
        this.markRelations.add(relation);
      });

    }
  }

  @JsonProperty("todoMarkIds")
  public List<Long> getTodoMarkIds() {
    if (markRelations == null) {
      return null;
    }
    return markRelations.stream()
        .map(r -> r.getTodoMark().getId())
        .collect(Collectors.toList());
  }

  @JsonProperty("todoMarks")
  public List<TodoMark> getTodoMarks() {
    if (markRelations == null) {
      return null;
    }
    return markRelations.stream()
        .map(TodoMarkRelation::getTodoMark)
        .collect(Collectors.toList());
  }
  
  @JsonIgnore
  public List<TodoMarkRelation> getMarkRelations() {
    return markRelations;
  }

  public void setMarkRelations(List<TodoMarkRelation> markRelations) {
    this.markRelations = markRelations;
  }

  public void syncUserIdToRelations() {
    if (this.markRelations != null && this.userId != null) {
      for (TodoMarkRelation relation : this.markRelations) {
        relation.setUserId(this.userId);
        relation.setTodoId(this.id);
      }
    }
  }

  public Todo() {}

  public Todo(String title, boolean completed) {
    this.title = title;
    this.completed = completed;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public Boolean getCompleted() { return completed; }
  public void setCompleted(Boolean completed) { this.completed = completed; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public String getTags() { return tags; }
  public void setTags(String tags) { this.tags = tags; }
  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }
  public LocalDateTime getPlannedFinishTime() { return plannedFinishTime; }
  public void setPlannedFinishTime(LocalDateTime plannedFinishTime) { this.plannedFinishTime = plannedFinishTime; }

  public String[] getReminders() {
    return reminders;
  }

  public void setReminders(String[] reminders) {
    this.reminders = reminders;
  }
}
