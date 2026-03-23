package com.example.learn.mapper;

import com.example.learn.entity.TodoMarkRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TodoMarkRelationMapper {
    void insert(TodoMarkRelation relation);
    List<TodoMarkRelation> findByTodoIdIn(@Param("todoIds") List<Long> todoIds);
    void deleteByTodoId(@Param("todoId") Long todoId);
}
