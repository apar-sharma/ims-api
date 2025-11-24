package com.apsharma.ims_api.comment.controller;

import com.apsharma.ims_api.comment.dto.CommentRequest;
import com.apsharma.ims_api.comment.dto.CommentResponse;
import com.apsharma.ims_api.comment.service.CommentService;
import com.apsharma.ims_api.util.ApiResponseBuilder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable Long postId, @RequestBody @Valid CommentRequest request) {
        CommentResponse comment = commentService.create(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseBuilder()
                        .status(HttpStatus.CREATED)
                        .message("Comment created successfully")
                        .data(comment)
                        .build()
        );
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Map<String, Object>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.listByPost(postId);
        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Comments retrieved successfully")
                        .data(comments)
                        .build()
        );
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable Long id) {
        CommentResponse comment = commentService.get(id);
        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Comment retrieved successfully")
                        .data(comment)
                        .build()
        );
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long id, @RequestBody @Valid CommentRequest request) {
        CommentResponse comment = commentService.update(id, request);
        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Comment updated successfully")
                        .data(comment)
                        .build()
        );
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

