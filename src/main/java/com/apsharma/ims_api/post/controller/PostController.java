package com.apsharma.ims_api.post.controller;

import com.apsharma.ims_api.post.dto.PostRequest;
import com.apsharma.ims_api.post.dto.PostResponse;
import com.apsharma.ims_api.post.dto.PostStatusUpdateRequest;
import com.apsharma.ims_api.post.service.PostService;
import com.apsharma.ims_api.util.ApiResponseBuilder;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody @Valid PostRequest request) {
        PostResponse post = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseBuilder()
                        .status(HttpStatus.CREATED)
                        .message("Post created successfully")
                        .data(post)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long createdById,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String dir
    ) {
        // Whitelist allowed sort fields
        String[] allowedSortFields = {"createdAt", "updatedAt", "title", "submittedAt"};
        String sortField = sort;
        boolean isValidSort = false;
        for (String field : allowedSortFields) {
            if (field.equalsIgnoreCase(sort)) {
                sortField = field;
                isValidSort = true;
                break;
            }
        }
        if (!isValidSort) {
            sortField = "createdAt";
        }

        // Create pageable with sort direction
        Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // Get filtered and paginated results
        Page<PostResponse> postsPage = postService.list(q, status, type, createdById, pageable);

        // Build pagination metadata
        Map<String, Object> pageMetadata = new HashMap<>();
        pageMetadata.put("currentPage", postsPage.getNumber());
        pageMetadata.put("totalPages", postsPage.getTotalPages());
        pageMetadata.put("totalItems", postsPage.getTotalElements());
        pageMetadata.put("pageSize", postsPage.getSize());
        pageMetadata.put("hasNext", postsPage.hasNext());
        pageMetadata.put("hasPrevious", postsPage.hasPrevious());

        // Build response with content and pagination metadata
        Map<String, Object> data = new HashMap<>();
        data.put("content", postsPage.getContent());
        data.put("pagination", pageMetadata);

        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Posts retrieved successfully")
                        .data(data)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable Long id) {
        PostResponse post = postService.get(id);
        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Post retrieved successfully")
                        .data(post)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequest request) {
        PostResponse post = postService.update(id, request);
        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Post updated successfully")
                        .data(post)
                        .build()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable Long id, @RequestBody @Valid PostStatusUpdateRequest request) {
        PostResponse post = postService.updateStatus(id, request);
        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Post status updated successfully")
                        .data(post)
                        .build()
        );
    }

}
