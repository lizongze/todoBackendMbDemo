package com.example.learn.mapper;

import com.example.learn.entity.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TodoMapper {
    List<Todo> findAll();
    Todo findById(@Param("id") Long id);
    void insert(Todo todo);
    void update(Todo todo);
    void deleteById(@Param("id") Long id);
}
