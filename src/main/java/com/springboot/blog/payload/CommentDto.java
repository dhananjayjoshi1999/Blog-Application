package com.springboot.blog.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CommentDto {

    private Long id;
    //comment name must not be empty or null
    @NotEmpty(message = "name must not be empty")
    private String name;
    //comment email must not be null or empty
    @NotEmpty(message = "Email must not be empty")
    @Email
    private String email;
    //comment body must not be emptyor null
    //comment body should have minimum 10 character.
    @NotEmpty(message = "Comment body must not be empty")
    @Size( min = 10,message = "Comment body must have 10 Character.")
    private String body;
}
