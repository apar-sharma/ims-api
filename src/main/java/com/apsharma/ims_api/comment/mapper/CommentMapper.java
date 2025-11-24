package com.apsharma.ims_api.comment.mapper;

import com.apsharma.ims_api.comment.dto.CommentRequest;
import com.apsharma.ims_api.comment.dto.CommentResponse;
import com.apsharma.ims_api.comment.model.Comment;
import com.apsharma.ims_api.post.model.Post;
import com.apsharma.ims_api.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentRequest request, Post post, User creator) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setCreatedBy(creator);
        return comment;
    }

    public void updateEntity(Comment comment, CommentRequest request) {
        comment.setContent(request.getContent());
    }

    public CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());

        if (comment.getPost() != null) {
            response.setPostId(comment.getPost().getId());
        }

        if (comment.getCreatedBy() != null) {
            response.setCreatedById(comment.getCreatedBy().getId());
            response.setCreatedByUsername(comment.getCreatedBy().getUsername());
        }

        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());

        return response;
    }
}

