package com.apsharma.ims_api.post.model;

import com.apsharma.ims_api.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Lob
    @NotBlank
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PostTag type = PostTag.ISSUE; // Default value is ISSUE

    @Enumerated(EnumType.STRING)
    @NotNull
    private PostStatus status = PostStatus.DRAFT; // Default value is DRAFT


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime submittedAt;

    private LocalDateTime resolvedAt;
}
