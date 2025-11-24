package com.apsharma.ims_api.post.dto;

import com.apsharma.ims_api.post.model.PostTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;


    @NotNull
    private PostTag type;

    @NotNull
    @Positive
    private Long createdById;
}
