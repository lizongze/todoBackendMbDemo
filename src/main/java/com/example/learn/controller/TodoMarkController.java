package com.example.learn.controller;

import com.example.learn.common.Result;
import com.example.learn.entity.TodoMark;
import com.example.learn.service.TodoMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = "*")
public class TodoMarkController {

    @Autowired
    private TodoMarkService todoMarkService;

    @GetMapping
    public Result<List<TodoMark>> getAllMarks() {
        return Result.success(todoMarkService.findAll());
    }

    @PostMapping
    public Result<TodoMark> createMark(@RequestBody TodoMark todoMark) {
        String title = todoMark.getTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag title is required");
        }
        return Result.success(todoMarkService.create(todoMark));
    }
}
