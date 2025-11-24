package com.apsharma.ims_api.post.controller;

import com.apsharma.ims_api.post.dto.PostRequest;
import com.apsharma.ims_api.post.dto.PostResponse;
import com.apsharma.ims_api.post.dto.PostStatusUpdateRequest;
import com.apsharma.ims_api.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO -> Use api builder to build responses

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(request));
    }

//    @GetMapping
//    public ResponseEntity<List<PostResponse>> getAllPosts() {
//        return ResponseEntity.ok(postService.list());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequest request) {
        return ResponseEntity.ok(postService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PostResponse> updateStatus(@PathVariable Long id, @RequestBody @Valid PostStatusUpdateRequest request) {
        return ResponseEntity.ok(postService.updateStatus(id, request));
    }
}
