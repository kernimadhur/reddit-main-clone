package com.example.demo.controller;

import com.example.demo.dto.CommentsDto;
import com.example.demo.service.CommentsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor

public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping
    public ResponseEntity<Object> createComment(@RequestBody CommentsDto commentsDto){
            commentsService.save(commentsDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId){
               return ResponseEntity.status(HttpStatus.OK).body(commentsService.getAllCommentsforPost(postId));
    }

    @GetMapping("by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(HttpStatus.OK).body(commentsService.getAllCommentsforUser(userName));
    }
}
