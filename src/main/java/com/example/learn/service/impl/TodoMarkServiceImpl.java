package com.example.learn.service.impl;

import com.example.learn.entity.TodoMark;
import com.example.learn.mapper.TodoMarkMapper;
import com.example.learn.service.TodoMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoMarkServiceImpl implements TodoMarkService {

    @Autowired
    private TodoMarkMapper todoMarkMapper;

    @Override
    public List<TodoMark> findAll() {
        return todoMarkMapper.findAll();
    }

    @Override
    public TodoMark create(TodoMark todoMark) {
        todoMarkMapper.insert(todoMark);
        return todoMark;
    }
}
