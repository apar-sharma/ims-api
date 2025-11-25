package com.apsharma.ims_api.post.service;

import com.apsharma.ims_api.post.dto.PostRequest;
import com.apsharma.ims_api.post.dto.PostResponse;
import com.apsharma.ims_api.post.dto.PostStatusUpdateRequest;
import com.apsharma.ims_api.post.mapper.PostMapper;
import com.apsharma.ims_api.post.model.Post;
import com.apsharma.ims_api.post.model.PostStatus;
import com.apsharma.ims_api.post.model.PostTag;
import com.apsharma.ims_api.post.repository.PostRepo;
import com.apsharma.ims_api.post.repository.PostSpecification;
import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.repository.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PostService {
    // constructor injection or autowire?

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final PostMapper postMapper;

    public PostService(PostRepo postRepo, UserRepo userRepo, PostMapper postMapper) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.postMapper = postMapper;
    }

    public PostResponse create(PostRequest request) {
        User creator = userRepo.findById(request.getCreatedById())
                .orElseThrow(() -> new IllegalArgumentException("Creator not found"));
        Post post = postMapper.toEntity(request, creator);
        Post saved = postRepo.save(post);
        return postMapper.toResponse(saved);
    }

    public Page<PostResponse> list(String q, String status, String type, Long createdById, Pageable pageable) {
        // Parse enum values from strings
        PostStatus postStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            postStatus = PostStatus.valueOf(status.toUpperCase());
        }

        PostTag postTag = null;
        if (type != null && !type.trim().isEmpty()) {
            postTag = PostTag.valueOf(type.toUpperCase());
        }

        // Build specification
        Specification<Post> spec = PostSpecification.build(q, postStatus, postTag, createdById);

        // Fetch and map to response
        Page<Post> postsPage = postRepo.findAll(spec, pageable);
        return postsPage.map(postMapper::toResponse);
    }

    public PostResponse get(Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return postMapper.toResponse(post);
    }

    public PostResponse update(Long id, PostRequest request) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        postMapper.updateEntity(post, request);
        Post saved = postRepo.save(post);

        return postMapper.toResponse(saved);
    }

    public PostResponse updateStatus(Long id, PostStatusUpdateRequest request) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setStatus(request.getStatus());

        if (request.getStatus() == PostStatus.RESOLVED) {
            post.setResolvedAt(LocalDateTime.now());
        }

        Post saved = postRepo.save(post);
        return postMapper.toResponse(saved);
    }
}
