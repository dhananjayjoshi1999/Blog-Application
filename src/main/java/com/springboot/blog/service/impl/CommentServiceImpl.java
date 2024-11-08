package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.metadata.CommonsDbcp2DataSourcePoolMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.mapper= mapper;
        this.commentRepository = commentRepository;
        this.postRepository= postRepository;
    }


    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        //convert Dto to entity
        Comment comment= mapToEntity(commentDto);

        //check the post id
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id", postId));

        comment.setPost(post);

        // Save comment in DB
        Comment response= commentRepository.save(comment);

        CommentDto commentDtoResp= mapToDto(comment);


        return commentDtoResp;
    }



    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        //retrieve post detail by id
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id", postId));
        //retrieve comment from DB
        List<Comment> comments= commentRepository.findByPostId(post.getId());
        //Convert list of comment to list of comment DTO
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByPost(long postId, long commentId) {
        //retrieve post detail by id
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id", postId));

        //retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "CommentId", commentId));

        //Check if Comment belongs to post
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }else{
            return mapToDto(comment);
        }


    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        //retrieve post detail by id
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id", postId));

        //retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "CommentId", commentId));

        //Check if Comment belongs to post
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.CONFLICT, "Comment does not belong to post");
        }
        else{
            comment.setName(commentRequest.getName());
            comment.setEmail(commentRequest.getEmail());
            comment.setBody(commentRequest.getBody());

            return mapToDto(commentRepository.save(comment));
        }
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        //retrieve post detail by id
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id", postId));

        //retrieve comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "CommentId", commentId));

        //Check if Comment belongs to post
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }else{
            commentRepository.deleteById(commentId);
        }

    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto= mapper.map(comment, CommentDto.class);
//        CommentDto commentDto= new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
    }
    private Comment mapToEntity(CommentDto commentDto){
        Comment comment= mapper.map(commentDto, Comment.class);
//        Comment comment= new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;

    }
}
