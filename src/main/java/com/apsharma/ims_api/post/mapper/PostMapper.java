package com.apsharma.ims_api.post.mapper;

import com.apsharma.ims_api.post.dto.PostRequest;
import com.apsharma.ims_api.post.dto.PostResponse;
import com.apsharma.ims_api.post.model.Post;
import com.apsharma.ims_api.post.model.PostStatus;
import com.apsharma.ims_api.user.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component


// Mapper class for converting between Post class and respective DTOs
// TODO -> Use mapstruct library to reduce boilerplate code ( or rather modelmapper library ) or is it fine as is?

public class PostMapper {

    public Post toEntity(PostRequest request, User creator) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setType(request.getType());
        post.setCreatedBy(creator);
        post.setStatus(PostStatus.DRAFT);
        post.setSubmittedAt(LocalDateTime.now());
        return post;
    }

    public void updateEntity(Post post, PostRequest request) {
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setType(request.getType());
    }

    public PostResponse toResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setType(post.getType());
        response.setStatus(post.getStatus());

        if (post.getCreatedBy() != null) {
            response.setCreatedByUsername(post.getCreatedBy().getUsername());
            response.setCreatedById(post.getCreatedBy().getId());
        }

        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setSubmittedAt(post.getSubmittedAt());
        response.setResolvedAt(post.getResolvedAt());

        return response;
    }
}

