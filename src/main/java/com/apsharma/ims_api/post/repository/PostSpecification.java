package com.apsharma.ims_api.post.repository;

import com.apsharma.ims_api.post.model.Post;
import com.apsharma.ims_api.post.model.PostStatus;
import com.apsharma.ims_api.post.model.PostTag;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PostSpecification {

    public static Specification<Post> build(String q, PostStatus status, PostTag type, Long createdById) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Free-text search on title and content
            if (q != null && !q.trim().isEmpty()) {
                String searchPattern = "%" + q.toLowerCase() + "%";
                Predicate titleMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        searchPattern
                );
                Predicate contentMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("content")),
                        searchPattern
                );
                predicates.add(criteriaBuilder.or(titleMatch, contentMatch));
            }

            // Filter by status
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Filter by type
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            // Filter by creator
            if (createdById != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy").get("id"), createdById));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
