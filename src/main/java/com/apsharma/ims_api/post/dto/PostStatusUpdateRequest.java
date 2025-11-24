package com.apsharma.ims_api.post.dto;

import com.apsharma.ims_api.post.model.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// DTO for updating the status of a post
@Getter
@Setter
@NoArgsConstructor
public class PostStatusUpdateRequest {
    @NotNull
    private PostStatus status;
}
