package com.apsharma.ims_api.post.repository;

import com.apsharma.ims_api.post.model.Post;
import com.apsharma.ims_api.post.model.PostStatus;
import com.apsharma.ims_api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
//    List<Post> findAllByStatus(PostStatus status);
//    List<Post> findAllByCreatedBy(User createdBy);
//    List<Post> findAllByCreatedByAndStatus(User createdBy, PostStatus status);
//    List<Post> findAllByAssignedTo(User assignedTo);
//    List<Post> findAllByAssignedToAndStatus(User assignedTo, PostStatus status);
//    Optional<Post> findFirstByAssignedToOrderByCreatedAtDesc(User assignedTo);
}
