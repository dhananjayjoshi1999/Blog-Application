package com.springboot.blog.controller;


import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(
        name = "CURD REST APIs for Post resource"
)
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(
            summary = "Create POst REST API",
            description = "THis post API used to save latest Post details to Database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO){

        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get All Post REST API",
            description = "THis post API used to get all Post details from Database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 SUCCESS"
    )
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false ) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value= "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value= "sortDir", defaultValue =AppConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(postService.getAllPosts(pageNo,pageSize, sortBy, sortDir),HttpStatus.OK);
    }

    @Operation(
            summary = "Get Post by ID REST API",
            description = "THis post API used to get Post by ID from Database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 SUCCESS"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(
            summary = "Update Post by Id REST API",
            description = "THis post API used to update Post details to Database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO, @PathVariable("id") long id){
        PostDTO response= postService.updatePost(postDTO, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Post by Id REST API",
            description = "THis post API used to delete Post details to Database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") long id){
        postService.deletePostById(id);
        return  new ResponseEntity<>("Post with "+id+" deleted successfully",HttpStatus.OK);
    }


    //Get Rest api for psot find by cateogry id
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDTO>> getPostByCategoryId(@PathVariable("id") Long categoryId){
        List<PostDTO> postDTOS=postService.getPostByCategoryId(categoryId);
        return new ResponseEntity<>(postDTOS,HttpStatus.OK);
    }





}
