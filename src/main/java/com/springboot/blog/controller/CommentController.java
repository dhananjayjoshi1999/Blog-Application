package com.springboot.blog.controller;


import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId
            ,@Valid @RequestBody CommentDto commentDto){
        CommentDto response= commentService.createComment(postId,commentDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable(name = "postId") long postId){
        List<CommentDto> allComment=commentService.getCommentByPostId(postId);
        return new ResponseEntity<>(allComment, HttpStatus.OK);
    }

    @GetMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(name = "postId") Long postId,
                                                     @PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.ok(commentService.getCommentByPost(postId, commentId));
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId,
            @Valid @RequestBody CommentDto commentDto
    ){
        CommentDto commentDtoResp= commentService.updateComment(postId, commentId, commentDto);
        return ResponseEntity.ok(commentDtoResp);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId
    ){
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok("comment deleted successfully");

    }
}
