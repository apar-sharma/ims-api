package com.apsharma.ims_api.post.service;

import com.apsharma.ims_api.post.dto.PostRequest;
import com.apsharma.ims_api.post.dto.PostResponse;
import com.apsharma.ims_api.post.dto.PostStatusUpdateRequest;
import com.apsharma.ims_api.post.mapper.PostMapper;
import com.apsharma.ims_api.post.model.Post;
import com.apsharma.ims_api.post.model.PostStatus;
import com.apsharma.ims_api.post.repository.PostRepo;
import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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


    // TODO -> Pagination for list of posts and reimplement this logic
//    public List<PostResponse> list() {
//        return postRepo.findAll().stream()
//                .map(postMapper::toResponse)
//                .toList();
//    }

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
