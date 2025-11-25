package com.apsharma.ims_api.post.repository;

import com.apsharma.ims_api.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepo extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
//    List<Post> findAllByStatus(PostStatus status);
//    List<Post> findAllByCreatedBy(User createdBy);
//    List<Post> findAllByCreatedByAndStatus(User createdBy, PostStatus status);
//    List<Post> findAllByAssignedTo(User assignedTo);
//    List<Post> findAllByAssignedToAndStatus(User assignedTo, PostStatus status);
//    Optional<Post> findFirstByAssignedToOrderByCreatedAtDesc(User assignedTo);
}
