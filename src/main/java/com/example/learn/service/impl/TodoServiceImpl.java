package com.example.learn.service.impl;

import cn.hutool.core.util.IdUtil;
import com.example.learn.entity.Todo;
import com.example.learn.mapper.TodoMapper;
import com.example.learn.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.learn.entity.TodoMarkRelation;
import com.example.learn.mapper.TodoMarkRelationMapper;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoServiceImpl implements TodoService {

  @Autowired private TodoMapper todoMapper;
  @Autowired private TodoMarkRelationMapper relationMapper;

  @Override
  public List<Todo> findAll() {
    List<Todo> todos = todoMapper.findAll();
    if (todos.isEmpty()) {
        return todos;
    }

    List<Long> todoIds = todos.stream().map(Todo::getId).collect(Collectors.toList());
    List<TodoMarkRelation> relations = relationMapper.findByTodoIdIn(todoIds);

    Map<Long, List<TodoMarkRelation>> relationMap = relations.stream()
        .collect(Collectors.groupingBy(TodoMarkRelation::getTodoId));

    for (Todo todo : todos) {
        List<TodoMarkRelation> todoRelations = relationMap.get(todo.getId());
        if (todoRelations != null) {
            todo.setMarkRelations(todoRelations);
        } else {
            todo.setMarkRelations(new ArrayList<>());
        }
    }

    return todos;
  }

  @Override
  @Transactional
  public Todo create(Todo todo) {
    todoMapper.insert(todo);
    // 保存关联表
    if (todo.getMarkRelations() != null && !todo.getMarkRelations().isEmpty()) {
        for (TodoMarkRelation relation : todo.getMarkRelations()) {
            relation.setTodoId(todo.getId());
            relation.setUserId(todo.getUserId() != null ? todo.getUserId() : 0L);
            relationMapper.insert(relation);
        }
    }
    return todo;
  }

  @Override
  @Transactional
  public Todo update(Long id, Todo todoDetails) {
    Todo todo = todoMapper.findById(id);
    if (todo == null) {
        throw new RuntimeException("Todo not found with id: " + id);
    }
    
    if (todoDetails.getTitle() != null) todo.setTitle(todoDetails.getTitle());
    if (todoDetails.getCompleted() != null) todo.setCompleted(todoDetails.getCompleted());
    if (todoDetails.getDescription() != null) todo.setDescription(todoDetails.getDescription());
    if (todoDetails.getPlannedFinishTime() != null) todo.setPlannedFinishTime(todoDetails.getPlannedFinishTime());
    if (todoDetails.getReminders() != null) todo.setReminders(todoDetails.getReminders());
    if (todoDetails.getStatus() != null) todo.setStatus(todoDetails.getStatus());
    if (todoDetails.getTags() != null) todo.setTags(todoDetails.getTags());
    
    todoMapper.update(todo);

    if (todoDetails.getTodoMarkIds() != null) {
        relationMapper.deleteByTodoId(id);
        todo.setTodoMarkIds(todoDetails.getTodoMarkIds());
        if (todo.getMarkRelations() != null) {
            for (TodoMarkRelation relation : todo.getMarkRelations()) {
                relation.setTodoId(id);
                relation.setUserId(todo.getUserId() != null ? todo.getUserId() : 0L);
                relationMapper.insert(relation);
            }
        }
    }
    
    return todo;
  }

  @Override
  public void delete(Long id) {
    relationMapper.deleteByTodoId(id);
    todoMapper.deleteById(id);
  }
}
