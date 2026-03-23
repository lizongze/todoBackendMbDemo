package com.example.learn.mapper;

import com.example.learn.entity.TodoMark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TodoMarkMapper {
    List<TodoMark> findAll();
    void insert(TodoMark todoMark);
}
