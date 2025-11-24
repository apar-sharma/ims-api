package com.apsharma.ims_api.post.dto;

import com.apsharma.ims_api.post.model.PostStatus;
import com.apsharma.ims_api.post.model.PostTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private PostTag type;
    private PostStatus status;
    private String createdByUsername;
    private Long createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime resolvedAt = null; // Stays null till the post is resolved
}
