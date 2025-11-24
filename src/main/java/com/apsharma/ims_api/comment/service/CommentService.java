package com.apsharma.ims_api.comment.service;

import com.apsharma.ims_api.comment.dto.CommentRequest;
import com.apsharma.ims_api.comment.dto.CommentResponse;
import com.apsharma.ims_api.comment.mapper.CommentMapper;
import com.apsharma.ims_api.comment.model.Comment;
import com.apsharma.ims_api.comment.repository.CommentRepo;
import com.apsharma.ims_api.post.model.Post;
import com.apsharma.ims_api.post.repository.PostRepo;
import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.repository.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// TODO: Exception handling needs improvement, by creating custom exceptions

@Service
@Transactional
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepo commentRepo, PostRepo postRepo, UserRepo userRepo, CommentMapper commentMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.commentMapper = commentMapper;
    }

    public CommentResponse create(Long postId, CommentRequest request) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepo.findByUsername(username);
        if (creator == null) {
            throw new IllegalArgumentException("User not found");
        }

        Comment comment = commentMapper.toEntity(request, post, creator);
        Comment saved = commentRepo.save(comment);
        return commentMapper.toResponse(saved);
    }

    public List<CommentResponse> listByPost(Long postId) {
        return commentRepo.findAllByPostId(postId).stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    public CommentResponse get(Long id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        return commentMapper.toResponse(comment);
    }

    public CommentResponse update(Long id, CommentRequest request) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        checkAuthorization(comment);

        commentMapper.updateEntity(comment, request);
        Comment saved = commentRepo.save(comment);
        return commentMapper.toResponse(saved);
    }

    public void delete(Long id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        checkAuthorization(comment);

        commentRepo.deleteById(id);
    }

    private void checkAuthorization(Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User currentUser = userRepo.findByUsername(username);
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        boolean isOwner = comment.getCreatedBy().getId().equals(currentUser.getId());
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new SecurityException("You are not authorized to perform this action");
        }
    }
}

