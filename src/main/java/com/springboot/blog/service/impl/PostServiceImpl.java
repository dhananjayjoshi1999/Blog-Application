package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper,
                           CategoryRepository categoryRepository) {
        this.mapper=mapper;
        this.postRepository = postRepository;
        this.categoryRepository= categoryRepository;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Category category= categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));
        //convert DTO to entity

//        Post post = new Post();
//        post.setTitle(postDTO.getTitle());
//        post.setDescription(postDTO.getDescription());
//        post.setContent(postDTO.getContent());
        Post post = mapToEntity(postDTO);
        post.setCategory(category);

        Post response= postRepository.save(post);
        // convert Entity to DTO
        PostDTO postDTO1= mapToDTO(response);
        return postDTO1;
    }




    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort= sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts= postRepository.findAll(pageable);
        List<Post> listOfPage= posts.getContent();
        List<PostDTO> content= listOfPage.stream().map(post-> mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse= new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getNumberOfElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;

//        return List.of();
    }

    @Override
    public PostDTO getPostById(long id) {
        Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        Post post= postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Post", "id", id));
        Category category= categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(
                ()-> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        post.setCategory(category);

        postRepository.save(post);

        return mapToDTO(post);
    }

    @Override
    public void deletePostById(long id) {
        Post post= postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
        System.out.println("Successfully deleted");
    }

    @Override
    public List<PostDTO> getPostByCategoryId(Long categoryId) {

        Category category= categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("category","Id", categoryId)
        );
        List<Post> posts= postRepository.findByCategoryId(categoryId);
        List<PostDTO> response = posts.stream().map(this::mapToDTO).collect(Collectors.toList());

        return response;
    }

    //convert entity to DTO object
    private PostDTO mapToDTO(Post post){
        PostDTO postDTO= mapper.map(post, PostDTO.class);
//        PostDTO postDTO= new PostDTO();
//        postDTO.setId(post.getId());
//        postDTO.setTitle(post.getTitle());
//        postDTO.setDescription(post.getDescription());
//        postDTO.setContent(post.getContent());
        return postDTO;
    }

    //convert DTP to entity object
    private Post mapToEntity(PostDTO postDTO){
        Post post = mapper.map(postDTO,Post.class);
//        Post post = new Post();
//        post.setTitle(postDTO.getTitle());
//        post.setDescription(postDTO.getDescription());
//        post.setContent(postDTO.getContent());
        return post;
    }
}
