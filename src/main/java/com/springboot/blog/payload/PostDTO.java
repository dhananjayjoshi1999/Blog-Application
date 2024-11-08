package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;


@Data
@Schema(description = "PostDto Model Information")
public class PostDTO {

    private Long id;
    //Title should not be null or empty and atleast have 2 character

    @Schema(description = "Blog Post  title")
    @NotEmpty
    @Size(min = 2, message = "Post title should have atleast 2 char.")
    private String title;

    @Schema(description = "Blog Post description")
    @NotEmpty
    @Size(min = 10, message = "Post description should have atleast 10 char.")
    private String description;

    @Schema(description = "Blog Post content")
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;

    @Schema(description = "Blog Post category")
    private Long categoryId;

}
